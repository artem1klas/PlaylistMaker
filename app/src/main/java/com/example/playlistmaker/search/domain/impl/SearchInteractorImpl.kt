package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.search.data.dto.Resource
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : TrackSearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: TrackSearchInteractor.TrackSearchConsumer) {
        executor.execute {
            when(val resource = repository.search(expression)) {
                is Resource.Succes -> {consumer.consume(resource.data, null)}
                is Resource.Error -> {consumer.consume(null, resource.message)}
            }
        }
    }
}