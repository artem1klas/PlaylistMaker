package com.example.playlistmaker.search.data

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.SearchRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TypeError
import java.util.Locale

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun search(expression: String): Resource<List<Track>> {
        val responce = networkClient.doRequest(TrackSearchRequest(expression))

        return when (responce.resultCode) {
            -1 -> {
                Resource.Error(TypeError.NO_CONNECTION)
            }
            200 -> {
                Resource.Succes((responce as TrackResponse).results
                    .map {
                        Track(
                            trackId = it.trackId ?: "-1",
                            trackName = it.trackName ?: "",
                            artistName = it.artistName ?.trim() ?: "",
                            trackTimeMillis = timeFormatter(it.trackTimeMillis),
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName ?: "",
                            releaseDate = it.releaseDate?.substring(0, 4) ?: "",
                            primaryGenreName = it.primaryGenreName ?: "",
                            country = it.country ?: "",
                            previewUrl = it.previewUrl ?: it.artworkUrl100
                        )
                    }
                )
            }
            else -> {
                Resource.Error(TypeError.SERVER_ERROR)
            }
        }

    }

    fun timeFormatter(t: String?): String {
        val time = t?.toInt() ?: 0
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time).toString()
    }
}