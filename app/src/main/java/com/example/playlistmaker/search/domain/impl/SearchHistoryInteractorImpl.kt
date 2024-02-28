package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(
    val searchHistoryRepository: SearchHistoryRepository
) : SearchHistoryInteractor{
    override fun read(): List<Track> {
        return searchHistoryRepository.read()
    }

    override fun add(track: Track) {
        searchHistoryRepository.add(track)
    }

    override fun clear() {
        searchHistoryRepository.clear()
    }


}