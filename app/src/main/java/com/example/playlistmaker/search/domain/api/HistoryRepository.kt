package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryRepository {
    fun read(): List<Track>
    fun add(track: Track)
    fun clear()
}