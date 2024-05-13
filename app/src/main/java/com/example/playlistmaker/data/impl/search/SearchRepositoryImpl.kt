package com.example.playlistmaker.data.impl.search

import com.example.playlistmaker.data.converters.TrackDtoConvertor
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.api_impl.search.SearchRepository
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TypeError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackDtoConvertor: TrackDtoConvertor
) : SearchRepository {
    override fun search(expression: String): Flow<Resource<List<Track>>> = flow {
        val responce = networkClient.doRequest(TrackSearchRequest(expression))

        when (responce.resultCode) {
            -1 -> {
                emit(Resource.Error(TypeError.NO_CONNECTION))
            }

            200 -> {
                with(responce as TrackResponse) {
                    val data = results
                        .map { trackDto ->
                            trackDtoConvertor.map(trackDto)
                        }
                    emit(Resource.Succes(data))
                }
            }

            else -> {
                emit(Resource.Error(TypeError.SERVER_ERROR))
            }
        }

    }
}