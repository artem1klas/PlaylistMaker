package com.example.playlistmaker.ui.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistBottomsheetLayoutBinding
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.utils.dpToPx


class PlaylistBottomsheetViewHolder(private val binding: PlaylistBottomsheetLayoutBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.namePlaylist
        binding.playlistSize.text = playlist.size.toString()

        Glide.with(itemView)
            .load(playlist.uri)
            .placeholder(R.drawable.image_placeholdertrack)
            .transform(CenterCrop(), RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.playlistButtomsheetImage)

    }
}