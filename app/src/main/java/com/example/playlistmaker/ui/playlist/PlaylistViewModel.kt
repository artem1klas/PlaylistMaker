package com.example.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {


    private val liveData = MutableLiveData<PlaylistState>(PlaylistState.Loading)

    fun observeState(): LiveData<PlaylistState> = liveData

    fun fillData(id: Int){
        viewModelScope.launch {
            playlistInteractor.getPlaylist(id)
                .collect{
                        playlist ->
                    processResult(playlist)
                }
        }
    }



//    fun getPlaylist(id: Int){
//        viewModelScope.launch {
//            playlistInteractor.getPlaylist(id)
//                .collect{
//                        playlist ->
//                    processResult(playlist)
//                }
//        }
//    }

    private fun processResult(playlist: Playlist) {
        renderState(PlaylistState.Content(playlist))
    }

    private fun renderState(state: PlaylistState) {
        liveData.postValue(state)
    }

}