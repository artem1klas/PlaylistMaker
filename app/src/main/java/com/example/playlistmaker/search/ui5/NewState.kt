package com.example.playlistmaker.search.ui5

import com.example.playlistmaker.search.domain.models.Track

sealed interface NewState {
    object Loading : NewState

    data class Content(val tracks: List<Track>) : NewState

    data class History(val tracks: List<Track>) : NewState

    data class NoConnection(val lastInput: String) : NewState

    object Empty : NewState

    object NotFound : NewState
}