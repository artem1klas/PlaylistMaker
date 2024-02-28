package com.example.playlistmaker.search.ui5


import android.app.Application
import android.content.SharedPreferences
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


class NewSearchViewModel(
    application: Application,
    sharedPreferences: SharedPreferences
) : AndroidViewModel(application) {


    private var lastSearchText: String? = null

    private val searchInteractor = Creator.provideTrackSearchInteractor(application)
    private val historyInteractor = Creator.provideSearchHistoryInteractor(sharedPreferences)

    fun addHistory(track: Track) {
        historyInteractor.add(track)
    }
    fun clearHistory() {
        historyInteractor.clear()
    }
    fun readHistory() {
        val tracks = historyInteractor.read()
        if (tracks.isEmpty()) {
            stateLiveData.postValue(NewState.Empty)
        } else {
            stateLiveData.postValue(NewState.History(tracks))
        }
    }




    val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText && stateLiveData.value !is NewState.NoConnection) {
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
            renderState(NewState.Loading)
        }

        searchInteractor.search(input, object : TrackSearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                when {
                    errorMessage != null -> {
                        renderState(NewState.NoConnection(input))
                    }

                    foundTracks.isNullOrEmpty() -> {
                        renderState(NewState.NotFound)
                    }

                    else -> {
                        renderState(NewState.Content(foundTracks))
                    }
                }
            }
        })
    }

    private val stateLiveData = MutableLiveData<NewState>(NewState.Empty)
    fun observeState(): LiveData<NewState> = stateLiveData

    private fun renderState(state: NewState) {
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

        fun getViewModelFactory(sharedPreferences: SharedPreferences): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NewSearchViewModel(
                    this[APPLICATION_KEY] as Application,
                    sharedPreferences
                )
            }
        }
    }
}


