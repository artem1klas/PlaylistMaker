package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.search.data.dto.Resource
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.api.TrackSearchRepository
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TrackSearchRepository) : TrackSearchInteractor {
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