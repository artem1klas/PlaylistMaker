package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(
    val searchHistoryRepository: SearchHistoryRepository
) : HistoryInteractor{
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