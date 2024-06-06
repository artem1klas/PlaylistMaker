package com.example.playlistmaker.domain.api_impl.media

import android.net.Uri

class NewPlaylistInteractorImpl(val playlistRepository: NewPlaylistRepository): NewPlaylistInteractor{
    override fun createNewPlaylist(name: String, description: String, uri: Uri?){
        playlistRepository.createNewPlaylist(name, description, uri)
    }
}