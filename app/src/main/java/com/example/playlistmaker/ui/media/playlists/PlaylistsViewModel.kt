package com.example.playlistmaker.ui.media.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.NewPlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TypeError
import com.example.playlistmaker.ui.media.favoritetracks.FavoriteState
import com.example.playlistmaker.ui.search.SearchState
import kotlinx.coroutines.launch

class PlaylistsViewModel(val newPlaylistInteractor: NewPlaylistInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData
    fun fillData() {
        renderState(PlaylistsState.Loading)
        viewModelScope.launch {
            newPlaylistInteractor
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

}