package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

const val MAX_HISTORY_LIST_SIZE = 10
const val HISTORY_KEY = "history_key"
const val HISTORY_SHARED_PREFENCES = "history_shared_preferences"

class HistoryRepositoryImpl(val context: Context) : SearchHistoryRepository {

    val sharedPreferenc = context.getSharedPreferences(HISTORY_SHARED_PREFENCES, MODE_PRIVATE)

    override fun read(): List<Track> {
        val json = sharedPreferenc.getString(HISTORY_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun add(track: Track) {
        var tempHistoryList = read().toMutableList()
        tempHistoryList.removeIf { it.trackId == track.trackId }
        tempHistoryList.add(0, track)
        if (tempHistoryList.size > MAX_HISTORY_LIST_SIZE) {
            tempHistoryList = tempHistoryList.subList(0, MAX_HISTORY_LIST_SIZE)
        }
        sharedPreferenc.edit()
            .putString(HISTORY_KEY, Gson().toJson(tempHistoryList))
            .apply()
    }

    override fun clear() {
        sharedPreferenc.edit().clear().apply()
    }
}