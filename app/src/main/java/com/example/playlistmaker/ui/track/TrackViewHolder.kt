package com.example.playlistmaker.ui.track

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.domain.models.Track

class TrackViewHolder(private val binding: TrackLayoutBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackTime.text = track.trackTimeMillis
        binding.artistName.text = track.artistName
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.image_placeholdertrack)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.trackImage)

    }
}