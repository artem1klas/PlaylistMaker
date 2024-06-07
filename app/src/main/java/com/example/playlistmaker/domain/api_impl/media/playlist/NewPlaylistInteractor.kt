package com.example.playlistmaker.domain.api_impl.media.playlist

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow


interface NewPlaylistInteractor{
    suspend fun createNewPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}