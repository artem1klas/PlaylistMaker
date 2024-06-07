package com.example.playlistmaker.data.impl.media

import android.net.Uri
import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.PlaylistDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.api_impl.media.FavoriteTrackRepository
import com.example.playlistmaker.domain.api_impl.media.NewPlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewPlaylistRepositoryImpl(
        private val playlistDatabase: PlaylistDatabase,
        private val playlistDbConvertor: PlaylistDbConvertor
): NewPlaylistRepository {
        override suspend fun createNewPlaylist(playlist: Playlist) {
                playlistDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
        }
}
//        override suspend fun addTrack(track: Track) {
//                appDatabase.trackDao().addTrack(trackDbConvertor.map(track))
//        }
//
//        override suspend fun removeTrack(track: Track) {
//                appDatabase.trackDao().removeTrack(trackDbConvertor.map(track))
//        }
//
//        override fun getTracks(): Flow<List<Track>> = flow {
//                val tracks = appDatabase.trackDao().getTracks()
//                emit(convertFromTrackEntity(tracks))
//        }
//
//        override fun getIdFavoriteTracks(): Flow<List<String>> = flow {
//                val idOfFavorites = appDatabase.trackDao().getIdFavoriteTracks()
//                emit(idOfFavorites)
//        }
//
//        private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
//                return tracks.map {
//                                track -> trackDbConvertor.map(track)
//                }
//        }

