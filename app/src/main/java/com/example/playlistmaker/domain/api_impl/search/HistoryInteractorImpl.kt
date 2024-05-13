package com.example.playlistmaker.domain.api_impl.search

import com.example.playlistmaker.domain.models.Track

class HistoryInteractorImpl(
    val historyRepository: HistoryRepository
) : HistoryInteractor {
    override fun read(): List<Track> {
        return historyRepository.read()
    }

    override fun add(track: Track) {
        historyRepository.add(track)
    }

    override fun clear() {
        historyRepository.clear()
    }


}