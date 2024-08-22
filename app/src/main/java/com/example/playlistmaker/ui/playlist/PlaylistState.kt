package com.example.playlistmaker.ui.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistState {
    object Loading: PlaylistState

    data class Content(val playlist: Playlist, val tracks: List<Track>): PlaylistState
}