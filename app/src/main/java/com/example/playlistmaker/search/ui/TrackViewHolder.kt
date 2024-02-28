package com.example.playlistmaker.search.ui

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.search.domain.models.Track
import java.util.Locale

class TrackViewHolder(private val binding: TrackLayoutBinding) : RecyclerView.ViewHolder(binding.root)
 {
    fun bind(track: Track, listener: OnItemClickListener) {
        binding.trackName.text = track.trackName
        binding.trackTime.text = track.trackTimeMillis
        binding.artistName.text = track.artistName
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.image_placeholdertrack)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.trackImage)
        itemView.setOnClickListener {
            listener
        }

    }
    interface OnItemClickListener {
        fun onClick(track: Track)
    }
}

