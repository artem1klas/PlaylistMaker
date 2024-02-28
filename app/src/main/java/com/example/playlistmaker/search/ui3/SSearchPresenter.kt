package com.example.playlistmaker.search.ui3

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui2.SearchPresenter
import moxy.MvpPresenter


class SSearchPresenter(
    context: Context
) : MvpPresenter<SSearchView>() {

    private var lastSearchText: String? = null

    private val interactor = Creator.provideTrackSearchInteractor(context)

    val handler = Handler(Looper.getMainLooper())

//    private val searchRunnable = Runnable {
//        val newSearchText = lastSearchText ?: ""
//        search(newSearchText)
//    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        this.lastSearchText = changedText

        val searchRunnable = Runnable {
            search(changedText)
        }

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        handler.postDelayed(searchRunnable, SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY_MILLIS)

//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    fun search(input: String) {
        if (input.isNotEmpty()) {
            renderState(SState.Loading)
        }

        interactor.search(input, object : TrackSearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                handler.post {
                    when {
                        errorMessage != null -> {
                            renderState(SState.NoConnection(input))
                        }

                        foundTracks.isNullOrEmpty() -> {
                            renderState(SState.NotFound)
                        }

                        else -> {
                            renderState(SState.Content(foundTracks))
                        }
                    }
                }
            }
        })
    }

    private fun renderState(state: SState) {
        viewState.render(state)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}


