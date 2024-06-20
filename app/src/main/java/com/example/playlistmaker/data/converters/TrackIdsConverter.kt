package com.example.playlistmaker.data.converters

import androidx.room.TypeConverter

class TrackIdsConverter {
    @TypeConverter
    fun fromList(list: ArrayList<String>): String {
        return list.joinToString { "," }
    }

    @TypeConverter
    fun toList(string: String): ArrayList<String> {
        val list = ArrayList<String>()
        val ids = string.split(",").toTypedArray()
        list.addAll(ids)
        return list
    }
}