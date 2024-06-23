package com.example.playlistmaker.data.impl.media

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.converters.TrackInPlaylistDbConvertor
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.database_dao.AppDatabase
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackInPlaylistDbConvertor: TrackInPlaylistDbConvertor
): PlaylistRepository {
    override suspend fun createNewPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val tracks = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(tracks))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlist.trackIds.add(track.trackId)
        playlist.size += 1
        playlistDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
        playlistDatabase.playlistDao().addTrackToPlaylist(trackInPlaylistDbConvertor.map(track))
    }

    private fun convertFromPlaylistEntity(tracks: List<PlaylistEntity>): List<Playlist>{
        return tracks.map {
                playlist -> playlistDbConvertor.map(playlist)
        }
    }
}