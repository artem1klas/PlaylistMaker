package com.example.playlistmaker.search.ui4

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchHistory
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.TrackViewHolder
import com.google.gson.Gson

class SSSearch : AppCompatActivity(), TrackViewHolder.OnItemClickListener {

    private lateinit var viewModel: SearchViewModel


    private var lastInput = ""
    private var sharedPrefHistory: SharedPreferences? = null
    private lateinit var searchHistory: SearchHistory

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(this, tracks)

    private var handler: Handler? = null
    private var isClickAllowed = true

    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("A12", "Activity   in oncreate")
        super.onCreate(savedInstanceState)


        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]


        sharedPrefHistory = getSharedPreferences(HISTORY_SHARED_PREFENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefHistory!!)
        handler = Handler(Looper.getMainLooper())

        binding.windowTrackList.layoutManager = LinearLayoutManager(this)
        binding.windowTrackList.adapter = adapter

        binding.hystoryList.adapter = adapter
        binding.hystoryList.layoutManager = LinearLayoutManager(this)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.queryInput.text.isNullOrEmpty()) {
                render(SSState.History(tracks))
            }
        }

        binding.clearButton.setOnClickListener {
            binding.queryInput.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
            binding.queryInput.clearFocus()
            render(SSState.History(tracks))
        }

        binding.updateButton.setOnClickListener {
            viewModel?.searchDebounce(lastInput)
        }

        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            render(SSState.Empty)
        }

        binding.queryInput.addTextChangedListener(textWatcher)

        viewModel.observeState().observe(this) {
            Log.d("A12", "Activity   in observe")
            Log.d("A12", "Activity   state observe is ${stateOfState()}")
//            binding.queryInput.clearFocus()
            render(it)
        }


        Log.d("A12", "Visible of elements (end oncreate):\n" +
        "windowDisconnect ${binding.windowDisconnect.isVisible}\n"+
        "windowNotFound ${binding.windowNotFound.isVisible}\n" +
        "windowTrackList ${binding.windowTrackList.isVisible}\n" +
        "windowHistory ${binding.windowHistory.isVisible}\n" +
        "windowProgressBar ${binding.windowProgressBar.isVisible}\n")
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            binding.clearButton.visibility = clearButtonVisibility(p0)
            render(SSState.Empty)
            if (!p0.isNullOrEmpty()) {
                viewModel?.searchDebounce(p0.toString())
            } else {
                render(SSState.History(tracks))
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun render(state: SSState) {
        when (state) {
            is SSState.Empty -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SSState.Content -> {
                Log.d("A12", "Activity   in render content")
                tracks.clear()
                tracks.addAll(state.tracks)
                Log.d("A12", "ACTIVITY    tracks is empty - ${tracks.isNullOrEmpty()}")
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = true
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

//            is SSState.NotFound -> {
//                Log.d("A12", "Activity   in render not_found")
//                binding.windowDisconnect.visibility = View.GONE
//                binding.windowNotFound.visibility = View.VISIBLE
//                binding.windowTrackList.visibility = View.GONE
//                binding.windowHistory.visibility = View.GONE
//                binding.windowProgressBar.visibility = View.GONE
//            }

            is SSState.NotFound -> {
                Log.d("A12", "Activity   in render not_found")
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = true
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SSState.NoConnection -> {
                this.lastInput = state.lastInput
                binding.windowDisconnect.isVisible = true
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SSState.History -> {
                tracks.clear()
                tracks.addAll(searchHistory.read())
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = searchHistory.read().isNotEmpty()
                binding.windowProgressBar.isVisible = false
            }

            is SSState.Loading -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = true
            }
        }
       adapter.notifyDataSetChanged()
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

    override fun onStart() {
        super.onStart()
        render(viewModel.observeState().value!!)
        Log.d("A12", "Visible of elements (in onstart):\n" +
                "windowDisconnect ${binding.windowDisconnect.isVisible}\n"+
                "windowNotFound ${binding.windowNotFound.isVisible}\n" +
                "windowTrackList ${binding.windowTrackList.isVisible}\n" +
                "windowHistory ${binding.windowHistory.isVisible}\n" +
                "windowProgressBar ${binding.windowProgressBar.isVisible}\n")
    }


    override fun onDestroy() {
        super.onDestroy()
        textWatcher.let { binding.queryInput.removeTextChangedListener(it) }
        Log.d("A12", "Activity   ------  destroy --------")

    }


    companion object {
        const val HISTORY_SHARED_PREFENCES = "history_shared_preferences"
        const val SELECTED_TRACK = "selected_track"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

    private fun stateOfState(): String {
        return when(viewModel.observeState().value) {
            is SSState.Content -> "content"
            is SSState.History -> "history"
            is SSState.NoConnection -> "no connection"
            is SSState.Empty -> "empty"
            is SSState.NotFound -> "not found"
            is SSState.Loading -> "loading"
            else -> "error"
        }
    }
}
