package com.example.playlistmaker.data.impl.media

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.data.db.tracks.AppDatabase
import com.example.playlistmaker.domain.api_impl.media.playlist.NewPlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewPlaylistRepositoryImpl(
    private val playlistDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
): NewPlaylistRepository {
    override suspend fun createNewPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val tracks = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(tracks))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistDatabase.playlistDao().addTrackToPlaylist(playlistDbConvertor.map(playlist))
    }

    private fun convertFromPlaylistEntity(tracks: List<PlaylistEntity>): List<Playlist>{
        return tracks.map {
                playlist -> playlistDbConvertor.map(playlist)
        }
    }

}
