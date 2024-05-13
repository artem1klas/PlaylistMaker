package com.example.playlistmaker.domain.api_impl.search


import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TypeError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {


    override fun search(expression: String): Flow<Pair<List<Track>?, TypeError?>> {
        return repository.search(expression).map{ result ->
            when(result) {
                is Resource.Succes -> {Pair(result.data, null)}
                is Resource.Error -> {Pair(null, result.typeError)}
            }
        }
    }
}