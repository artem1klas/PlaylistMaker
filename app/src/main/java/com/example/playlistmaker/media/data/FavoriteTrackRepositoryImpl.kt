package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.domain.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.models.Track
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

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map {
            track -> trackDbConvertor.map(track)
        }
    }

}