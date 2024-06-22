package com.example.playlistmaker.data.impl.media

import com.example.playlistmaker.data.db.entities.TrackEntity
import com.example.playlistmaker.data.db.database_dao.AppDatabase
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.domain.api_impl.media.favorite_track.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTrackRepository {
    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().addTrack(trackDbConvertor.map(track))
    }

    override suspend fun removeTrack(track: Track) {
        appDatabase.trackDao().removeTrack(trackDbConvertor.map(track))
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getIdFavoriteTracks(): Flow<List<String>> = flow {
        val idOfFavorites = appDatabase.trackDao().getIdFavoriteTracks()
        emit(idOfFavorites)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map {
            track -> trackDbConvertor.map(track)
        }
    }

}