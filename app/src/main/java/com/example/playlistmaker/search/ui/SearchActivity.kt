package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class SearchActivity : AppCompatActivity(), TrackViewHolder.OnItemClickListener {

    val interactor = Creator.provideTrackSearchInteractor(this)


    private var textSearch = ""
    private var lastInputText = ""
    private var sharedPrefHistory: SharedPreferences? = null
    private lateinit var searchHistory: SearchHistory

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(this, tracks)

    private var handler: Handler? = null
    private var isClickAllowed = true

    private lateinit var binding: ActivitySearchBinding

//    private var trackList: RecyclerView? = null
//    private var historyList: RecyclerView? = null
//    private var backButton: Button? = null
//    private var queryInput: EditText? = null
//    private var clearButton: Button? = null
//    private var updateButton: Button? = null
//    private var clearHistoryButton: Button? = null
//    private var windowDisconnect: LinearLayout? = null
//    private var windowNotFound: LinearLayout? = null
//    private var windowTrackList: RecyclerView? = null
//    private var windowHistory: LinearLayout? = null
//    private var windowProgressBar: ProgressBar? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefHistory = getSharedPreferences(HISTORY_SHARED_PREFENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefHistory!!)
        handler = Handler(Looper.getMainLooper())


//        trackList = findViewById(R.id.windowTrackList)
//        windowTrackList = findViewById(R.id.windowTrackList)
//        historyList = findViewById(R.id.hystoryList)
//        windowHistory = findViewById(R.id.windowHistory)
//        backButton = findViewById(R.id.backButton)
//        queryInput = findViewById(R.id.queryInput)
//        clearButton = findViewById(R.id.clearButton)
//        updateButton = findViewById(R.id.updateButton)
//        clearHistoryButton = findViewById(R.id.clearHistoryButton)
//        windowDisconnect = findViewById(R.id.windowDisconnect)
//        windowNotFound = findViewById(R.id.windowNotFound)
//        windowProgressBar = findViewById(R.id.windowProgressBar)



        binding.windowTrackList.layoutManager = LinearLayoutManager(this)
        binding.windowTrackList.adapter = adapter



        binding.hystoryList.adapter = adapter
        binding.hystoryList.layoutManager = LinearLayoutManager(this)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tracks.clear()
                tracks.addAll(searchHistory.read())
                render(State.History(tracks))
            }
        }

        binding.clearButton.setOnClickListener {
            textSearch = ""
            binding.queryInput.setText(textSearch)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
            tracks.clear()
            tracks.addAll(searchHistory.read())
            render(State.History(tracks))
        }

        binding.updateButton.setOnClickListener {
            search(lastInputText)
        }

        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            tracks.clear()
            tracks.addAll(searchHistory.read())
            render(State.History(tracks))
        }

        binding.queryInput.addTextChangedListener(searchTextWatcher)
    }

    private val searchRunnable = Runnable { search(binding.queryInput.text.toString()) }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.clearButton.visibility = clearButtonVisibility(p0)
            render(State.Empty)
            textSearch = p0.toString()
            if (!p0.isNullOrEmpty()) {
                searchDebounce()
            } else {
                tracks.clear()
                tracks.addAll(searchHistory.read())
                render(State.History(tracks))
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    fun search(input: String) {
        if (input.isNotEmpty()) {
            render(State.Loading)
        }

        interactor.search(input, object : TrackSearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                handler?.post {
                    if (foundTracks != null) {
                        tracks.clear()
                        tracks.addAll(foundTracks)
                    }
                    when {
                        errorMessage != null -> {
                            render(State.NoConnection)
                        }

                        tracks.isEmpty() -> {
                            render(State.NotFound)
                        }

                        else -> {
                            render(State.Content(tracks))
                        }
                    }
                }
            }
        })
    }

    fun render(state: State) {
        when (state) {
            is State.Empty -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is State.Content -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = true
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is State.NotFound -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = true
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is State.NoConnection -> {
                binding.windowDisconnect.isVisible = true
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is State.History -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = searchHistory.read().isNotEmpty()
                binding.windowProgressBar.isVisible = false
            }

            is State.Loading -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = true
            }

        }
        adapter.notifyDataSetChanged()
    }

    sealed interface State {
        object Loading : State

        data class Content(val tracks: List<Track>) : State

        data class History(val tracks: List<Track>) : State

        object NoConnection : State

        object Empty : State

        object NotFound : State
    }


    private fun searchDebounce() {
        handler?.removeCallbacks(searchRunnable)
        handler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(TEXT_SEARCH, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchHistory.add(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(SELECTED_TRACK, Gson().toJson(track))
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(searchRunnable)
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        const val HISTORY_SHARED_PREFENCES = "history_shared_preferences"
        const val SELECTED_TRACK = "selected_track"
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}








