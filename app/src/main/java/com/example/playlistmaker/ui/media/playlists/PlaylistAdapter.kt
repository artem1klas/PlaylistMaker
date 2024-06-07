package com.example.playlistmaker.ui.media.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.track.TrackViewHolder


class PlaylistAdapter(val playlists: ArrayList<Playlist>) : RecyclerView.Adapter<PlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistLayoutBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
    override fun getItemCount() = playlists.size

}