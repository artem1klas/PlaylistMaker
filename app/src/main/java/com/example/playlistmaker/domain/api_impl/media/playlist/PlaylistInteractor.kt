package com.example.playlistmaker.domain.api_impl.media.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface PlaylistInteractor{
    suspend fun createNewPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    fun getPlaylist(id: Int): Flow<Playlist>

    fun getTracksInPlaylist(idsTracks: MutableList<String>): Flow<List<Track>>

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(playlist: Playlist, trackId: String)

    suspend fun addOnlyTrack(track: Track)

}