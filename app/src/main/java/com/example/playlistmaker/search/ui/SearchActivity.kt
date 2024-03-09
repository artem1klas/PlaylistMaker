package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), TrackViewHolder.OnItemClickListener {

    private  val viewModel by viewModel<SearchViewModel>()

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
                viewModel.readHistory()
            }
        }

        binding.clearButton.setOnClickListener {
            binding.queryInput.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
            binding.queryInput.clearFocus()
            render(SearchActivityState.Empty)
        }

        binding.updateButton.setOnClickListener {
            val lastInput = (viewModel.observeState().value as SearchActivityState.NoConnection).lastInput
            viewModel.searchDebounce(lastInput)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.queryInput.addTextChangedListener(textWatcher)

        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.clearButton.visibility = clearButtonVisibility(p0)
            if (!p0.isNullOrEmpty()) {
                viewModel?.searchDebounce(p0.toString())
            } else {
                viewModel.readHistory()
            }
        }
        override fun afterTextChanged(p0: Editable?) {
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun render(state: SearchActivityState) {
        when (state) {
            is SearchActivityState.Empty -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchActivityState.Content -> {
                tracks.clear()
                tracks.addAll(state.tracks)
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = true
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchActivityState.NotFound -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = true
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchActivityState.NoConnection -> {
                binding.windowDisconnect.isVisible = true
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchActivityState.History -> {
                tracks.clear()
                tracks.addAll(state.tracks)
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = true
                binding.windowProgressBar.isVisible = false
            }

            is SearchActivityState.Loading -> {
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
            viewModel.addHistory(track)
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
        const val SELECTED_TRACK = "selected_track"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
