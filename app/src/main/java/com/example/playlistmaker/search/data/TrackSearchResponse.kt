package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class TrackSearchResponse {
    class TrackResponse(
        val results: List<TrackDto>
    ) : Response()
}