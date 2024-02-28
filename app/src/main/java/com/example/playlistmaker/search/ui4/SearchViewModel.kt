package com.example.playlistmaker.search.ui4

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
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


    private var lastSearchText: String? = null

    private val interactor = Creator.provideTrackSearchInteractor(application)


    val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText && stateLiveData.value !is SSState.NoConnection) {
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
            renderState(SSState.Loading)
        }

        interactor.search(input, object : TrackSearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    when {
                        errorMessage != null -> {
                            renderState(SSState.NoConnection(input))
                        }

                        foundTracks.isNullOrEmpty() -> {
                            renderState(SSState.NotFound)
                        }

                        else -> {
                            renderState(SSState.Content(foundTracks))
                        }
                    }
                }
        })
    }

    private val stateLiveData = MutableLiveData<SSState>(SSState.Empty)
    fun observeState(): LiveData<SSState> = stateLiveData

    private fun renderState(state: SSState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        Log.d("A12", "VM   ------  destroy --------")
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}


