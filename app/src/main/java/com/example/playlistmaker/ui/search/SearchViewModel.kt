package com.example.playlistmaker.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.search.HistoryInteractor
import com.example.playlistmaker.domain.api_impl.search.SearchInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TypeError
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private var lastSearchText: String? = null

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            search(changedText)
        }

    fun addHistory(track: Track) {
        historyInteractor.add(track)
    }

    fun clearHistory() {
        historyInteractor.clear()
        stateLiveData.postValue(SearchState.Empty)
    }

    fun readHistory(): List<Track> {
        val tracks = historyInteractor.read()
        if (tracks.isEmpty()) {
            stateLiveData.postValue(SearchState.Empty)
        } else {
            stateLiveData.postValue(SearchState.History(tracks))
        }
        return tracks
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText != changedText) {
            lastSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun search(input: String) {

        if (input.isNotEmpty()) {
            renderState(SearchState.Loading)
        }

        viewModelScope.launch {
            searchInteractor
                .search(input)
                .collect { pair ->
                    processResult(input, pair.first, pair.second)
                }
        }
    }

    private fun processResult(input: String, foundTracks: List<Track>?, typeError: TypeError?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }


        when {
            typeError != null -> {
                renderState(SearchState.NoConnection(input))
            }

            foundTracks.isNullOrEmpty() -> {
                renderState(SearchState.NotFound)
            }

            else -> {
                renderState(SearchState.Content(foundTracks))
            }
        }
    }

    private val stateLiveData = MutableLiveData<SearchState>(SearchState.Empty)
    fun observeState(): LiveData<SearchState> = stateLiveData

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}