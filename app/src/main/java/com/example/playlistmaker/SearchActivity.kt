package com.example.playlistmaker

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
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.presentation.AudioPlayerActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackViewHolder.OnItemClickListener {
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)
    private var textSearch = ""
    private var lastInputText = ""
    private var sharedPrefHistory: SharedPreferences? = null
    private lateinit var searchHistory: SearchHistory
    private var trackList: RecyclerView? = null
    private var historyList: RecyclerView? = null
    private val adapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)
    private var handler: Handler? = null
    private var isClickAllowed = true

    private var backButton: Button? = null
    private var queryInput: EditText? = null
    private var clearButton: Button? = null
    private var updateButton: Button? = null
    private var clearHistoryButton: Button? = null
    private var windowDisconnect: LinearLayout? = null
    private var windowNotFound: LinearLayout? = null
    private var windowTrackList: RecyclerView? = null
    private var widowHistory: LinearLayout? = null
    private var windowProgressBar: ProgressBar? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefHistory = getSharedPreferences(HISTORY_SHARED_PREFENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefHistory!!)
        handler = Handler(Looper.getMainLooper())

        backButton = findViewById(R.id.back)
        queryInput = findViewById(R.id.queryInput)
        clearButton = findViewById(R.id.clear)
        updateButton = findViewById(R.id.problem_updateButton)
        clearHistoryButton = findViewById(R.id.button_clear_history)
        windowDisconnect = findViewById(R.id.window_disconnect)
        windowNotFound = findViewById(R.id.window_not_found)
        windowTrackList = findViewById(R.id.window_trackList)
        widowHistory = findViewById(R.id.window_history)
        windowProgressBar = findViewById(R.id.window_progressBar)

        trackList = findViewById<RecyclerView>(R.id.window_trackList)
        trackList?.layoutManager = LinearLayoutManager(this)
        trackList?.adapter = adapter

        historyList = findViewById<RecyclerView>(R.id.hystory_trackList_recycle)
        historyList?.layoutManager = LinearLayoutManager(this)
        historyList?.adapter = historyAdapter
        historyAdapter.tracks.addAll(searchHistory.read())

        backButton?.setOnClickListener {
            finish()
        }

        queryInput?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateElementVisible(StatementType.HISTORY_MOMENT)
            }
        }

        clearButton?.setOnClickListener {
            textSearch = ""
            queryInput?.setText(textSearch)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton?.windowToken, 0)
            adapter.tracks.clear()
            historyAdapter.tracks.clear()
            historyAdapter.tracks.addAll(searchHistory.read())
            updateElementVisible(StatementType.HISTORY_MOMENT)
        }

        updateButton?.setOnClickListener {
            search(lastInputText)
        }

        clearHistoryButton?.setOnClickListener {
            searchHistory.clear()
            historyAdapter.tracks.clear()
            updateElementVisible(StatementType.HISTORY_MOMENT)
            historyAdapter.notifyDataSetChanged()
        }

        queryInput?.addTextChangedListener(searchTextWatcher)
    }

    private val searchRunnable = Runnable { search(queryInput?.text.toString()) }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            clearButton?.visibility = clearButtonVisibility(p0)
            updateElementVisible(StatementType.TRACKS_OR_EMPTY)
            textSearch = p0.toString()
            if(!p0.isNullOrEmpty()) {
                searchDebounce()
            } else {
                updateElementVisible(StatementType.HISTORY_MOMENT)
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }
    fun updateElementVisible(type: StatementType) {
        when (type) {
            StatementType.TRACKS_OR_EMPTY -> {
                windowDisconnect?.isVisible = false
                windowNotFound?.isVisible = false
                windowTrackList?.isVisible = true
                widowHistory?.isVisible = false
                windowProgressBar?.isVisible = false
            }

            StatementType.NOT_FOUND -> {
                windowDisconnect?.isVisible = false
                windowNotFound?.isVisible = true
                windowTrackList?.isVisible = false
                widowHistory?.isVisible = false
                windowProgressBar?.isVisible = false
            }

            StatementType.NO_CONNECTION -> {
                windowDisconnect?.isVisible = true
                windowNotFound?.isVisible = false
                windowTrackList?.isVisible = false
                widowHistory?.isVisible = false
                windowProgressBar?.isVisible = false
            }

            StatementType.HISTORY_MOMENT -> {
                windowDisconnect?.isVisible = false
                windowNotFound?.isVisible = false
                windowTrackList?.isVisible = false
                widowHistory?.isVisible = historyAdapter.tracks.isNotEmpty()
                windowProgressBar?.isVisible = false
            }

            StatementType.PROGRESS_BAR -> {
                windowDisconnect?.isVisible = false
                windowNotFound?.isVisible = false
                windowTrackList?.isVisible = false
                widowHistory?.isVisible = false
                windowProgressBar?.isVisible = true
            }

        }
        adapter.notifyDataSetChanged()
        historyAdapter.notifyDataSetChanged()
    }

    fun search(input: String) {
        if(input.isNotEmpty()) {
            updateElementVisible(StatementType.PROGRESS_BAR)
        } else {
            updateElementVisible(StatementType.HISTORY_MOMENT)
        }
        itunesService.search(input).enqueue(object :
            Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                val responseBody = response.body()
                if (response.code() == CODE_OK) {
                    adapter.tracks.clear()

                    if (responseBody?.results?.isNotEmpty() == true) {
                        adapter.tracks.addAll(responseBody.results!!)
                        updateElementVisible(StatementType.TRACKS_OR_EMPTY)
                    } else if (adapter.tracks.isEmpty() && input.isNotEmpty()) {
                        adapter.tracks.clear()
                        updateElementVisible(StatementType.NOT_FOUND)
                    }
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                adapter.tracks.clear()
                lastInputText = queryInput?.text.toString()
                updateElementVisible(StatementType.NO_CONNECTION)
            }

        })
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
            val intent = Intent(this, AudioPlayerActivity::class.java)
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
        private const val CODE_OK = 200
    }
}
enum class StatementType {
    NO_CONNECTION,
    NOT_FOUND,
    TRACKS_OR_EMPTY,
    HISTORY_MOMENT,
    PROGRESS_BAR
}








