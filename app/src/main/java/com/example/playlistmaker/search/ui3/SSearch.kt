package com.example.playlistmaker.search.ui3

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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchHistory
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.TrackViewHolder
import com.google.gson.Gson
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class SSearch : MvpActivity(), TrackViewHolder.OnItemClickListener, SSearchView {

    @InjectPresenter
    lateinit var searchPresenter: SSearchPresenter

    @ProvidePresenter
    fun providePresenter(): SSearchPresenter {
        return Creator.provideSSearchPresenter(this.applicationContext)
    }

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
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            if (hasFocus) {
                tracks.clear()
                tracks.addAll(searchHistory.read())
                render(SState.History(tracks))
            }
        }

        binding.clearButton.setOnClickListener {
            binding.queryInput.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
            tracks.clear()
            tracks.addAll(searchHistory.read())
            render(SState.History(tracks))
        }

        binding.updateButton.setOnClickListener {
            searchPresenter?.searchDebounce(lastInput)
        }

        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            render(SState.Empty)
        }

        binding.queryInput.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            binding.clearButton.visibility = clearButtonVisibility(p0)
            render(SState.Empty)
            if (!p0.isNullOrEmpty()) {
                searchPresenter?.searchDebounce(p0.toString())
            } else {
                render(SState.History(tracks))
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    override fun render(state: SState) {
        when (state) {
            is SState.Empty -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SState.Content -> {
                tracks.clear()
                tracks.addAll(state.tracks)
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = true
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SState.NotFound -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = true
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SState.NoConnection -> {
                lastInput = state.lastInput
                binding.windowDisconnect.isVisible = true
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SState.History -> {
                tracks.clear()
                tracks.addAll(searchHistory.read())
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = searchHistory.read().isNotEmpty()
                binding.windowProgressBar.isVisible = false
            }

            is SState.Loading -> {
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


    override fun onDestroy() {
        super.onDestroy()
        textWatcher.let { binding.queryInput.removeTextChangedListener(it) }
    }


    companion object {
        const val HISTORY_SHARED_PREFENCES = "history_shared_preferences"
        const val SELECTED_TRACK = "selected_track"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
