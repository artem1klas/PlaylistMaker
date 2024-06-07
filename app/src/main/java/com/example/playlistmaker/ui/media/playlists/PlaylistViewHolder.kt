package com.example.playlistmaker.ui.media.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.dpToPx

class PlaylistViewHolder(private val binding: PlaylistLayoutBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.namePlaylist
        binding.playlistSize.text = playlist.size.toString()


//        Glide.with(itemView)
//            .load(track.artworkUrl100)
//            .placeholder(R.drawable.image_placeholdertrack)
//            .fitCenter()
//            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
//            .into(binding.trackImage)

    }
}