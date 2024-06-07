package com.example.playlistmaker.domain.api_impl.media

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist

class NewPlaylistInteractorImpl(val playlistRepository: NewPlaylistRepository): NewPlaylistInteractor{
    override suspend fun createNewPlaylist(playlist: Playlist){
        playlistRepository.createNewPlaylist(playlist)
    }
}