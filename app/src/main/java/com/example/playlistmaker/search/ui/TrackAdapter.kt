package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.search.domain.models.Track


class TrackAdapter(private val listener: TrackViewHolder.OnItemClickListener, val tracks: ArrayList<Track>) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackLayoutBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
        holder.itemView.setOnClickListener{
            listener.onClick(tracks[position])
        }
    }
    override fun getItemCount() = tracks.size
}