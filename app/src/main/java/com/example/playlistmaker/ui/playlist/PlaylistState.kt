package com.example.playlistmaker.ui.playlist

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {
    object Loading: PlaylistState

    data class Content(val playlist: Playlist): PlaylistState
}