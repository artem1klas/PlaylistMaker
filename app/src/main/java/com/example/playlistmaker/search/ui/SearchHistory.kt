package com.example.playlistmaker.search.ui

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

const val MAX_HISTORY_LIST_SIZE = 10
const val HISTORY_KEY = "history_key"

class SearchHistory(sharedPrefences: SharedPreferences) {

    private val sharedPrefs = sharedPrefences

    fun read(): List<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY, null) ?: return emptyList<Track>()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun add(track: Track) {
        var tempHistoryList = read().toMutableList()
        tempHistoryList.removeIf { it.trackId == track.trackId }
        tempHistoryList.add(0, track)
        if (tempHistoryList.size > MAX_HISTORY_LIST_SIZE) {
            tempHistoryList = tempHistoryList.subList(0, MAX_HISTORY_LIST_SIZE)
        }
        sharedPrefs.edit()
            .putString(HISTORY_KEY, Gson().toJson(tempHistoryList))
            .apply()
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
    }

}