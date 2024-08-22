package com.example.playlistmaker.data.impl.media

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.converters.TrackInPlaylistDbConvertor
import com.example.playlistmaker.data.db.database_dao.AppDatabase
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.TrackInPlaylistEntity
import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackInPlaylistDbConvertor: TrackInPlaylistDbConvertor,
): PlaylistRepository {
    override suspend fun createNewPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlist.trackIds.add(track.trackId)
        appDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist.copy(
                    size = playlist.size + 1
        )))
        appDatabase.playlistDao().addTrackToPlaylist(trackInPlaylistDbConvertor.map(track))
    }

    override suspend fun addOnlyTrack(track: Track) {
        appDatabase.playlistDao().addTrackToPlaylist(trackInPlaylistDbConvertor.map(track))
    }



    override fun getPlaylist(id: Int): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylist(id)
        emit(playlistDbConvertor.map(playlist))
    }

    override fun getTracksInPlaylist(idsTracks: List<Int>): Flow<List<Track>> = flow {
        val tracks = appDatabase.playlistDao().getTracksInPlaylist(idsTracks)
        emit(convertFromTrackInPlaylistEntity(tracks))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, trackId: String) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
        val ids = convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylists()).map{it.trackIds}.flatten()
        if(!ids.contains(trackId)){
            appDatabase.playlistDao().deteteTrackFromPlaylist(trackId)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))
        val ids = convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylists()).map{it.trackIds}.flatten()
        for(trackId in playlist.trackIds){
            if (!ids.contains(trackId)){
                appDatabase.playlistDao().deteteTrackFromPlaylist(trackId)
            }
        }
    }

    private fun convertFromPlaylistEntity(tracks: List<PlaylistEntity>): List<Playlist>{
        return tracks.map {
                playlist -> playlistDbConvertor.map(playlist)
        }
    }
    private fun convertFromTrackInPlaylistEntity(tracks: List<TrackInPlaylistEntity>): List<Track>{
        return tracks.map {
                track -> trackInPlaylistDbConvertor.map(track)
        }
    }



}
