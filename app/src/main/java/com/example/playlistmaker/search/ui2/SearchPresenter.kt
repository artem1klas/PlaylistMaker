package com.example.playlistmaker.search.ui2

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.models.Track


class SearchPresenter(
    context: Context
) {

    private var view: SearchView? = null
    private var state: State? = null
    private var latestSearchText: String? = null

    fun attachView(view: SearchView) {
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView() {
        this.view = null
    }

    private var lastSearchText: String? = null

    val interactor = Creator.provideTrackSearchInteractor(context)

    val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        search(newSearchText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            this.latestSearchText = changedText
        }
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    fun search(input: String) {
        if (input.isNotEmpty()) {
            renderState(State.Loading)
        }

        interactor.search(input, object : TrackSearchInteractor.TrackSearchConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                handler.post {
                    when {
                        errorMessage != null -> {
                            renderState(State.NoConnection(input))
                        }

                        foundTracks.isNullOrEmpty() -> {
                            renderState(State.NotFound)
                        }

                        else -> {
                            renderState(State.Content(foundTracks))
                        }
                    }
                }
            }
        })
    }

    private fun renderState(state: State) {
        this.state = state
        this.view?.render(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }


}