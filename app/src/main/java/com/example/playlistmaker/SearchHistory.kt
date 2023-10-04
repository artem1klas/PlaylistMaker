package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val MAX_HISTORY_LIST_SIZE = 10
const val HISTORY_KEY = "history_key"

class SearchHistory(sharedPrefences: SharedPreferences) {

    val sharedPrefs = sharedPrefences

    fun read(): List<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY, null) ?: return emptyList<Track>()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun add(track: Track) {
        var tempHystoryList = read().toMutableList()
        tempHystoryList.removeIf { it.trackId == track.trackId }
        tempHystoryList.add(0, track)
        if (tempHystoryList.size > MAX_HISTORY_LIST_SIZE) {
            tempHystoryList = tempHystoryList.subList(0, MAX_HISTORY_LIST_SIZE)
        }
        sharedPrefs.edit()
            .putString(HISTORY_KEY, Gson().toJson(tempHystoryList))
            .apply()
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
    }

}