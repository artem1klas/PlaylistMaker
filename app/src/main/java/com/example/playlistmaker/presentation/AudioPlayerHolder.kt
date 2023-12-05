package com.example.playlistmaker.presentation

import com.example.*
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.dpToPx
import java.util.Locale

const val CURENT_TRACK_TIME = "00:00"

class AudioPlayerHolder(private val audioPlayerActivity: AudioPlayerActivity) {
    private val imageTrack = audioPlayerActivity.findViewById<ImageView>(R.id.imageTrack)
    private val trackName = audioPlayerActivity.findViewById<TextView>(R.id.trackName)
    private val artistName = audioPlayerActivity.findViewById<TextView>(R.id.trackArtist)
    private val currentTrackTime = audioPlayerActivity.findViewById<TextView>(R.id.currentTrackTime)
    private val trackTimeMillis = audioPlayerActivity.findViewById<TextView>(R.id.trackTimeMillisValue)
    private val album = audioPlayerActivity.findViewById<TextView>(R.id.albumValue)
    private val yearRelease = audioPlayerActivity.findViewById<TextView>(R.id.yearValue)
    private val genre = audioPlayerActivity.findViewById<TextView>(R.id.genreValue)
    private val country = audioPlayerActivity.findViewById<TextView>(R.id.countryValue)

    fun bind(track: Track) {
        val t = track.trackTimeMillis?.toInt() ?: 0
        trackName.text = track.trackName
        artistName.text = track.artistName
        currentTrackTime.text = CURENT_TRACK_TIME
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(t).toString()
        album.text = track.collectionName
        yearRelease.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country

        Glide.with(audioPlayerActivity)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.image_placeholdertrack)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, audioPlayerActivity.applicationContext)))
            .into(imageTrack)
    }

}