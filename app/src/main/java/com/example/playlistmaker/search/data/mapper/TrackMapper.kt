package com.example.playlistmaker.search.data.mapper

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import java.util.Locale

object TrackMapper {
    fun map(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId ?: "-1",
            trackName = trackDto.trackName ?: "",
            artistName = trackDto.artistName ?.trim() ?: "",
            trackTimeMillis = timeFormatter(trackDto.trackTimeMillis),
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName ?: "",
            releaseDate = trackDto.releaseDate?.substring(0, 4) ?: "",
            primaryGenreName = trackDto.primaryGenreName ?: "",
            country = trackDto.country ?: "",
            previewUrl = trackDto.previewUrl ?: trackDto.artworkUrl100
        )
    }

    fun timeFormatter(t: String?): String {
        val time = t?.toInt() ?: 0
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time).toString()
    }
}