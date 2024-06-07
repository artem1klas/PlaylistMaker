package com.example.playlistmaker.domain.api_impl.media

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist

interface NewPlaylistRepository{
    suspend fun createNewPlaylist(playlist: Playlist)
}