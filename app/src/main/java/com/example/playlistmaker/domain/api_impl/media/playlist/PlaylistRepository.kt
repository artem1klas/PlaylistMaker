package com.example.playlistmaker.domain.api_impl.media.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository{
    suspend fun createNewPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

   fun getPlaylist(id: Int): Flow<Playlist>

   fun getTracksInPlaylist(idsTracks: List<Int>): Flow<List<Track>>

}