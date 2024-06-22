package com.example.playlistmaker.ui.media.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Loading)
    fun observeState(): LiveData<PlaylistsState> = stateLiveData
    fun fillData() {
        renderState(PlaylistsState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistsState.Empty)
        } else {
            renderState(PlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist){
        viewModelScope.launch {
            if (trackAdded()) {

            } else {
                playlistInteractor.addTrackToPlaylist(track, playlist)
            }
        }
    }

    private fun trackAdded(): Boolean {
        return false
    }



}