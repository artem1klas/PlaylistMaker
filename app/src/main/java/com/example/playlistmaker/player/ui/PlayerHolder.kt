package com.example.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaPlayerBinding
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.search.domain.models.Track


class PlayerHolder(
    private val binding: MediaPlayerBinding,
    private val audioPlayerActivity: PlayerActivity
    ) {
    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTimeMillisValue.text = track.trackTimeMillis
        binding.albumValue.text = track.collectionName
        binding.yearValue.text = track.releaseDate
        binding.genreValue.text = track.primaryGenreName
        binding.countryValue.text = track.country

        Glide.with(binding.root)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.image_placeholdertrack)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, audioPlayerActivity.applicationContext)))
            .into(binding.imageTrack)
    }

}