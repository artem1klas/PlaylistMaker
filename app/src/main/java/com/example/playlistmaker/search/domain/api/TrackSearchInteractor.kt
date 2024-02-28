package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TrackSearchInteractor {
    fun search(expression: String, consumer: TrackSearchConsumer)


    interface TrackSearchConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}