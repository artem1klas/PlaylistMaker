package com.example.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBottomsheetLayoutBinding
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.track.TrackAdapter


class PlaylistBottomsheetAdapter(val playlists: ArrayList<Playlist>, private val listener: PlaylistClickListener) : RecyclerView.Adapter<PlaylistBottomsheetViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomsheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistBottomsheetViewHolder(PlaylistBottomsheetLayoutBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: PlaylistBottomsheetViewHolder, position: Int) {
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