package com.example.playlistmaker.domain.api_impl.media

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val favoriteTrackRepository: FavoriteTrackRepository
): FavoriteTrackInteractor {
    override suspend fun addTrack(track: Track) {
        favoriteTrackRepository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        favoriteTrackRepository.removeTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoriteTrackRepository.getTracks()
    }

    override fun getIdFavoriteTracks(): Flow<List<String>> {
        return favoriteTrackRepository.getIdFavoriteTracks()
    }
}