package com.example.playlistmaker.ui.adapters.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBottomsheetLayoutBinding
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist


class PlaylistAdapter(val playlists: ArrayList<Playlist>, private val listener: PlaylistClickListener) : RecyclerView.Adapter<PlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistLayoutBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            listener.onClick(playlists[position])
        }
    }
    override fun getItemCount() = playlists.size

    fun interface PlaylistClickListener {
        fun onClick(playlist: Playlist)
    }

}