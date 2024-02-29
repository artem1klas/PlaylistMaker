package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface SearchActivityState {
    object Loading : SearchActivityState

    data class Content(val tracks: List<Track>) : SearchActivityState

    data class History(val tracks: List<Track>) : SearchActivityState

    data class NoConnection(val lastInput: String) : SearchActivityState

    object Empty : SearchActivityState

    object NotFound : SearchActivityState
}