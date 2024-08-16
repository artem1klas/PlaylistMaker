package com.example.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {


    private val liveData = MutableLiveData<PlaylistState>(PlaylistState.Loading)

    fun observeState(): LiveData<PlaylistState> = liveData

    fun fillData(id: Int){
        var playlist: Playlist? = null
        var tracks = listOf<Track>()
        val playlistAsync = viewModelScope.async(Dispatchers.IO) {
            playlistInteractor.getPlaylist(id)
                .collect{
                    playlist = it
                }
        }
        runBlocking {
            playlistAsync.await()
        }
        val tracksAsync = viewModelScope.async(Dispatchers.IO) {
            playlistInteractor.getTracksInPlaylist(playlist?.trackIds?.toMutableList() ?: mutableListOf())
                .collect{
                tracks = it
            }
        }
       runBlocking {
           tracksAsync.await()
       }

        if (playlist != null){
            renderState(PlaylistState.Content(playlist!!, tracks))
        }
    }

    fun deleteTrackFromPlaylist(playlist: Playlist, trackId: String){
        viewModelScope.launch(Dispatchers.IO) {
            playlist.size --
            playlist.trackIds.remove(trackId)
//            playlistInteractor.updatePlaylist(playlist)
            playlistInteractor.deleteTrackFromPlaylist(playlist, trackId)

//            viewModelScope.launch(Dispatchers.IO) {
//
//            }

        }
    }

//    fun deleteTrack(trackId: String) {
//        val async =
//    }


//    override suspend fun addIdsPlaylistAndTrack(ids: IdsPlaylistAndTrack) {
//        playlistRepository.addIdsPlaylistAndTrack(ids)
//    }
//
//    override suspend fun deleteIdsPlaylistAndTrack(ids: IdsPlaylistAndTrack) {
//        playlistRepository.deleteIdsPlaylistAndTrack(ids)
//    }
//
//    override suspend fun getIdTrackInPlaylists(idTrack: String): Flow<List<String>> {
//        return playlistRepository.getIdTrackInPlaylists(idTrack)
//    }



//    fun getPlaylist(id: Int){
//        viewModelScope.launch {
//            playlistInteractor.getPlaylist(id)
//                .collect{
//                        playlist ->
//                    processResult(playlist)
//                }
//        }
//    }

//    private fun processResult(playlist: Playlist) {
//        renderState(PlaylistState.Content(playlist))
//    }

    private fun renderState(state: PlaylistState) {
        liveData.postValue(state)
    }

}