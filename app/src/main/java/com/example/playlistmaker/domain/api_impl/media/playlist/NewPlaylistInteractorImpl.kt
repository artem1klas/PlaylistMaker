package com.example.playlistmaker.domain.api_impl.media.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class NewPlaylistInteractorImpl(val playlistRepository: NewPlaylistRepository):
    NewPlaylistInteractor {
    override suspend fun createNewPlaylist(playlist: Playlist){
        playlistRepository.createNewPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }
}