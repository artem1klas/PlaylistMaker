package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(
    val historyRepository: HistoryRepository
) : HistoryInteractor{
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