package com.example.playlistmaker.domain.api_impl.media

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    fun getTracks(): Flow<List<Track>>

    fun getIdFavoriteTracks(): Flow<List<String>>
}