package com.example.playlistmaker.search.ui3

import com.example.playlistmaker.search.domain.models.Track

sealed interface SState {
    object Loading : SState

    data class Content(val tracks: List<Track>) : SState

    data class History(val tracks: List<Track>) : SState

    data class NoConnection(val lastInput: String) : SState

    object Empty : SState

    object NotFound : SState
}