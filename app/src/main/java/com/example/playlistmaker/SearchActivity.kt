package com.example.playlistmaker

import TrackAdapter
import TrackViewHolder
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var trackList: RecyclerView
    private lateinit var historyList: RecyclerView
    private val adapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)
//    private val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefHistory = getSharedPreferences(HISTORY_SHARED_PREFENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefHistory!!)

        val backButton = findViewById<Button>(R.id.back)
        val queryInput = findViewById<EditText>(R.id.queryInput)
        val clearButton = findViewById<Button>(R.id.clear)
        val updateButton = findViewById<Button>(R.id.problem_updateButton)
        val clearHistoryButton = findViewById<Button>(R.id.button_clear_history)
        val windowDisconnect = findViewById<LinearLayout>(R.id.window_disconnect)
        val windowNotFound = findViewById<LinearLayout>(R.id.window_not_found)
        val windowTrackList = findViewById<RecyclerView>(R.id.window_trackList)
        val widowHistory = findViewById<LinearLayout>(R.id.window_history)


        trackList = findViewById<RecyclerView>(R.id.window_trackList)
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = adapter

        historyList = findViewById<RecyclerView>(R.id.hystory_trackList_recycle)
        historyList.layoutManager = LinearLayoutManager(this)
        historyList.adapter = historyAdapter
        historyAdapter.tracks.addAll(searchHistory.read())

        fun updateElementVisible(type: StatementType) {
            when (type) {
                StatementType.TRACKS_OR_EMPTY -> {
                    windowDisconnect.isVisible = false
                    windowNotFound.isVisible = false
                    windowTrackList.isVisible = true
                    widowHistory.isVisible = false
                }

                StatementType.NOT_FOUND -> {
                    windowDisconnect.isVisible = false
                    windowNotFound.isVisible = true
                    windowTrackList.isVisible = false
                    widowHistory.isVisible = false
                }

                StatementType.NO_CONNECTION -> {
                    windowDisconnect.isVisible = true
                    windowNotFound.isVisible = false
                    windowTrackList.isVisible = false
                    widowHistory.isVisible = false

                }

                StatementType.HISTORY_MOMENT -> {
                    windowDisconnect.isVisible = false
                    windowNotFound.isVisible = false
                    windowTrackList.isVisible = false
                    widowHistory.isVisible = historyAdapter.tracks.isNotEmpty()
                }

            }
            adapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()
        }

        fun search(searchText: String) {
            itunesService.search(searchText).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    val responseBody = response.body()
                    if (response.code() == 200) {
                        adapter.tracks.clear()

                        if (responseBody?.results?.isNotEmpty() == true) {
                            adapter.tracks.addAll(responseBody.results!!)
                            updateElementVisible(StatementType.TRACKS_OR_EMPTY)
                        } else if (adapter.tracks.isEmpty()) {
                            adapter.tracks.clear()
                            updateElementVisible(StatementType.NOT_FOUND)
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    adapter.tracks.clear()
                    lastInputText = queryInput.text.toString()
                    updateElementVisible(StatementType.NO_CONNECTION)
                }

            })
        }

        backButton.setOnClickListener {
            finish()
        }

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(queryInput.text.toString())
            }
            false
        }

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateElementVisible(StatementType.HISTORY_MOMENT)
            }
        }

        clearButton.setOnClickListener {
            textSearch = ""
            queryInput.setText(textSearch)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            adapter.tracks.clear()
            historyAdapter.tracks.clear()
            historyAdapter.tracks.addAll(searchHistory.read())
            updateElementVisible(StatementType.HISTORY_MOMENT)
        }

        updateButton.setOnClickListener {
            search(lastInputText)
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            historyAdapter.tracks.clear()
            updateElementVisible(StatementType.HISTORY_MOMENT)
            historyAdapter.notifyDataSetChanged()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                updateElementVisible(StatementType.TRACKS_OR_EMPTY)
                textSearch = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        queryInput.addTextChangedListener(searchTextWatcher)
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

    override fun onClick(track: Track) {
        searchHistory.add(track)

        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(SELECTED_TRACK, Gson().toJson(track))
        startActivity(intent)
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        const val HISTORY_SHARED_PREFENCES = "history_shared_preferences"
        const val SELECTED_TRACK = "selected_track"
    }
}
enum class StatementType {
    NO_CONNECTION,
    NOT_FOUND,
    TRACKS_OR_EMPTY,
    HISTORY_MOMENT
}








