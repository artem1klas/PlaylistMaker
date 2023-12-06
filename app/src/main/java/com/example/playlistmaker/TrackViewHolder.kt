package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.Track
import java.util.Locale

class TrackViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_layout, parent, false)
) {

    private val image: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    fun bind(track: Track, listener: OnItemClickListener) {
        val t = track.trackTimeMillis?.toInt() ?: 0
        trackName.text = track.trackName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(t).toString()
        artistName.text = track.artistName.trim()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.image_placeholdertrack)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(image)
        itemView.setOnClickListener{
            listener
        }

    }

    interface OnItemClickListener {
        fun onClick(track: Track)
    }
}

