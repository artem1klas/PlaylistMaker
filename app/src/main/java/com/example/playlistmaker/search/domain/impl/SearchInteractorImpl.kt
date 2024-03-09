package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchInteractor.TrackSearchConsumer) {
        executor.execute {
            when(val resource = repository.search(expression)) {
                is Resource.Succes -> {consumer.consume(resource.data, null)}
                is Resource.Error -> {consumer.consume(null, resource.typeError)}
            }
        }
    }
}