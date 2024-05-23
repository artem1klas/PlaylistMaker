package com.example.playlistmaker.domain.api_impl.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TypeError
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun search(expression: String): Flow<Pair<List<Track>?, TypeError?>>

}