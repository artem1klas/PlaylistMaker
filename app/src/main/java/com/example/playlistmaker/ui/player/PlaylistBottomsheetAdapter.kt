package com.example.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBottomsheetLayoutBinding
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist


class PlaylistBottomsheetAdapter(val playlists: ArrayList<Playlist>) : RecyclerView.Adapter<PlaylistBottomsheetViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomsheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistBottomsheetViewHolder(PlaylistBottomsheetLayoutBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: PlaylistBottomsheetViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
    override fun getItemCount() = playlists.size

}