package com.example.playlistmaker.domain.api_impl.media

import android.net.Uri

interface NewPlaylistRepository{
    fun createNewPlaylist(name: String, description: String, uri: Uri?)
}