package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.Resource
import com.example.playlistmaker.search.domain.models.Track

interface TrackSearchRepository {
    fun search(expression: String): Resource<List<Track>>
}