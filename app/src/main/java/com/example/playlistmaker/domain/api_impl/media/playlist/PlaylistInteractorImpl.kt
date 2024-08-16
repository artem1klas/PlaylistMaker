package com.example.playlistmaker.domain.api_impl.media.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val playlistRepository: PlaylistRepository):
    PlaylistInteractor {
    override suspend fun createNewPlaylist(playlist: Playlist){
        playlistRepository.createNewPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun addOnlyTrack(track: Track) {
        playlistRepository.addOnlyTrack(track)
    }

    override fun getPlaylist(id: Int): Flow<Playlist> {
        return playlistRepository.getPlaylist(id)
    }

    override fun getTracksInPlaylist(idsTracks: MutableList<String>): Flow<List<Track>> {
        return playlistRepository.getTracksInPlaylist(idsTracks.map { it.toInt() })
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, trackId: String) {
        playlistRepository.deleteTrackFromPlaylist(playlist, trackId)
    }

}