package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    fun getTracks(): Flow<List<Track>>
}