package com.example.playlistmaker.ui.media.new_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    fun createNewPlaylist(namePlaylist: String, descriptionPlaylist: String, uri: String, track: Track?) {
        val trackIds = if (track != null) mutableListOf(track!!.trackId) else mutableListOf()
        val playlist = Playlist(
            id = 0,
            namePlaylist = namePlaylist,
            descriptionPlaylist = descriptionPlaylist,
            uri = uri,
            trackIds = trackIds,
            size = trackIds.size
        )
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(playlist)
        }
        if (track != null) {
            viewModelScope.launch {
                playlistInteractor.addOnlyTrack(track)
            }
        }
    }
}



