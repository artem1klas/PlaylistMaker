package com.example.playlistmaker.search.ui2

import com.example.playlistmaker.search.domain.models.Track

sealed interface State {
    object Loading : State

    data class Content(val tracks: List<Track>) : State

    data class History(val tracks: List<Track>) : State

    data class NoConnection(val lastInput: String) : State

    object Empty : State

    object NotFound : State
}