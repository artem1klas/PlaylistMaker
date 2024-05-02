package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TypeError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

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