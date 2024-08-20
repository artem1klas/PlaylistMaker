package com.example.playlistmaker.ui.media.edit_playlist

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.new_playlist.CreatePlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): CreatePlaylistViewModel(playlistInteractor) {

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

}