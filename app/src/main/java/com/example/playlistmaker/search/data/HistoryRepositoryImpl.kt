package com.example.playlistmaker.search.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

private const val MAX_HISTORY_LIST_SIZE = 10
private const val HISTORY_KEY = "history_key"

class HistoryRepositoryImpl(
    val context: Context,
    val sharedPreferences: SharedPreferences,
    val gson: Gson
) : HistoryRepository {

    override fun read(): List<Track> {
        val json = this.sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }

    override fun add(track: Track) {
        var tempHistoryList = read().toMutableList()
        tempHistoryList.removeIf { it.trackId == track.trackId }
        tempHistoryList.add(0, track)
        if (tempHistoryList.size > MAX_HISTORY_LIST_SIZE) {
            tempHistoryList = tempHistoryList.subList(0, MAX_HISTORY_LIST_SIZE)
        }
        this.sharedPreferences.edit()
            .putString(HISTORY_KEY, Gson().toJson(tempHistoryList))
            .apply()
    }

    override fun clear() {
        this.sharedPreferences.edit().clear().apply()
    }
}