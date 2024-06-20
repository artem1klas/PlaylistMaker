package com.example.playlistmaker.ui.media.new_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.NewPlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: NewPlaylistInteractor): ViewModel() {


    fun createNewPlaylist(namePlaylist: String, descriptionPlaylist: String, uri: String) {
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(Playlist(
                id = 0,
                namePlaylist = namePlaylist,
                descriptionPlaylist = descriptionPlaylist,
                uri = uri,
                trackIds = ArrayList<String>(),
                size = 0
            ))
        }

    }


}



