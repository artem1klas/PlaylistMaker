package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackViewHolder.OnItemClickListener {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

    private val viewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(this, tracks)

    private var handler: Handler? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        binding.windowTrackList.layoutManager = LinearLayoutManager(requireContext())
        binding.windowTrackList.adapter = adapter

        binding.hystoryList.adapter = adapter
        binding.hystoryList.layoutManager = LinearLayoutManager(requireContext())


        binding.queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.queryInput.text.isNullOrEmpty()) {
                viewModel.readHistory()
            }
        }

        binding.clearButton.setOnClickListener {
            binding.queryInput.setText("")
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
            binding.queryInput.clearFocus()
            render(SearchState.Empty)
        }

        binding.updateButton.setOnClickListener {
            val lastInput = (viewModel.observeState().value as SearchState.NoConnection).lastInput
            viewModel.searchDebounce(lastInput)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.queryInput.addTextChangedListener(textWatcher)

        viewModel.observeState().observe(viewLifecycleOwner) {
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
    fun render(state: SearchState) {
        when (state) {
            is SearchState.Empty -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchState.Content -> {
                tracks.clear()
                tracks.addAll(state.tracks)
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = true
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchState.NotFound -> {
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = true
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchState.NoConnection -> {
                binding.windowDisconnect.isVisible = true
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is SearchState.History -> {
                tracks.clear()
                tracks.addAll(state.tracks)
                binding.windowDisconnect.isVisible = false
                binding.windowNotFound.isVisible = false
                binding.windowTrackList.isVisible = false
                binding.windowHistory.isVisible = true
                binding.windowProgressBar.isVisible = false
            }

            is SearchState.Loading -> {
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
            findNavController().navigate(
                R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(trackId = Gson().toJson(track))
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher.let { binding.queryInput.removeTextChangedListener(it) }
        _binding = null
    }


}