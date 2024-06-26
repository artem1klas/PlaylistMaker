package com.example.playlistmaker.ui.adapters.playlist

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistBottomsheetLayoutBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.utils.declineTrack
import com.example.playlistmaker.utils.dpToPx

class PlaylistBottomsheetViewHolder(private val binding: PlaylistBottomsheetLayoutBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.namePlaylist
        binding.playlistSize.text = "${playlist.size} ${declineTrack(itemView.context, playlist.size)}"

        Glide.with(itemView)
            .load(playlist.uri)
            .placeholder(R.drawable.image_placeholdertrack)
            .transform(CenterCrop(), RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.playlistButtomsheetImage)

    }
}