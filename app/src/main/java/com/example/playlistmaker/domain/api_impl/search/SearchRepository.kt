package com.example.playlistmaker.domain.api_impl.search

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(expression: String): Flow<Resource<List<Track>>>
}