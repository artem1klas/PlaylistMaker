package com.example.playlistmaker.domain.api_impl.search

import com.example.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun read(): List<Track>
    fun add(track: Track)
    fun clear()
}