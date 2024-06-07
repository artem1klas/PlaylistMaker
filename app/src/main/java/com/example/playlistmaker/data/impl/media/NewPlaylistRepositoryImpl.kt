package com.example.playlistmaker.data.impl.media

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.playlists.PlaylistDatabase
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.api_impl.media.playlist.NewPlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewPlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
): NewPlaylistRepository {
    override suspend fun createNewPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val tracks = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(tracks))
    }

    private fun convertFromPlaylistEntity(tracks: List<PlaylistEntity>): List<Playlist>{
        return tracks.map {
                playlist -> playlistDbConvertor.map(playlist)
        }
    }

}
