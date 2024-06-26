package com.example.playlistmaker.ui.media.playlists

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistsState {
    object Loading : PlaylistsState

    object Empty : PlaylistsState

    data class Content(val playlists: List<Playlist>) : PlaylistsState
}

