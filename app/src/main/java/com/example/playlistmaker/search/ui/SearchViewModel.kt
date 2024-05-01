package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TypeError

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {


    val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

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
        if (lastSearchText == changedText && stateLiveData.value !is SearchState.NoConnection) {
            return
        }
        this.lastSearchText = changedText

        val searchRunnable = Runnable {
            search(changedText)
        }

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
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

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}