package com.example.playlistmaker.search.ui4

import com.example.playlistmaker.search.domain.models.Track

sealed interface SSState {
    object Loading : SSState

    data class Content(val tracks: List<Track>) : SSState

    data class History(val tracks: List<Track>) : SSState

    data class NoConnection(val lastInput: String) : SSState

    object Empty : SSState

    object NotFound : SSState
}