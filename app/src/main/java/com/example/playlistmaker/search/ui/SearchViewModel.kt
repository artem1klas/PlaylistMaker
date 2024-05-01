package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TypeError
import com.example.playlistmaker.utils.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private var lastSearchText: String? = null

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
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
        if(lastSearchText != changedText){
            lastSearchText = changedText
            trackSearchDebounce(changedText)
        }

    }

    fun search(input: String) {

        if (input.isNotEmpty()) {
            renderState(SearchState.Loading)
        }

        searchInteractor.search(input, object : SearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, typeError: TypeError?) {
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
        })
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