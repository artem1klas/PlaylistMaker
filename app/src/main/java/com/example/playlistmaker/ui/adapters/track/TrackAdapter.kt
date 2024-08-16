package com.example.playlistmaker.ui.adapters.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.models.Track


class TrackAdapter(
    val tracks: ArrayList<Track>,
    private val listener: TrackClickListener,
//    private val listenerLong: TrackLongClickListener = fun onClick(track: Track){}
) : RecyclerView.Adapter<TrackViewHolder>() {

    lateinit var onClickLong: ((Track) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackLayoutBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            listener.onClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onClickLong.invoke(tracks[position])
            true
        }
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }

//    fun interface TrackLongClickListener {
//        fun onClick(track: Track)
//    }
}