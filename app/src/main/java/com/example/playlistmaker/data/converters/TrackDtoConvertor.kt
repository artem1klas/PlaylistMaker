package com.example.playlistmaker.data.converters

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track
import java.util.Locale

class TrackDtoConvertor {
    fun map(track: TrackDto): Track {
        return Track(
            trackId = track.trackId ?: "-1",
            trackName = track.trackName?.trim() ?: "",
            artistName = track.artistName?.trim() ?: "",
            trackTimeMillis = timeFormatter(track.trackTimeMillis),
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate?.substring(0, 4) ?: "",
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: track.artworkUrl100
        )
    }

    fun timeFormatter(t: String?): String {
        val time = t?.toInt() ?: 0
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time).toString()
    }
}