package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val searchInteractor = Creator.provideSearchInteractor(application)
    private val historyInteractor = Creator.provideHistoryInteractor(application)

    val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    fun addHistory(track: Track) {
        historyInteractor.add(track)
    }

    fun clearHistory() {
        historyInteractor.clear()
        stateLiveData.postValue(SearchActivityState.Empty)
    }

    fun readHistory(): List<Track> {
        val tracks = historyInteractor.read()
        if (tracks.isEmpty()) {
            stateLiveData.postValue(SearchActivityState.Empty)
        } else {
            stateLiveData.postValue(SearchActivityState.History(tracks))
        }
        return tracks
    }


    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText && stateLiveData.value !is SearchActivityState.NoConnection) {
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
            renderState(SearchActivityState.Loading)
        }

        searchInteractor.search(input, object : TrackSearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                when {
                    errorMessage != null -> {
                        renderState(SearchActivityState.NoConnection(input))
                    }

                    foundTracks.isNullOrEmpty() -> {
                        renderState(SearchActivityState.NotFound)
                    }

                    else -> {
                        renderState(SearchActivityState.Content(foundTracks))
                    }
                }
            }
        })
    }

    private val stateLiveData = MutableLiveData<SearchActivityState>(SearchActivityState.Empty)
    fun observeState(): LiveData<SearchActivityState> = stateLiveData

    private fun renderState(state: SearchActivityState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        this[APPLICATION_KEY] as Application
                    )
                }
            }
    }
}


