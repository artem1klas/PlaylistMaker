package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search")
    suspend fun search(@Query("term") text: String): TrackResponse
}