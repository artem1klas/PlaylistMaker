import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Track

class TrackAdapter(private val listener: TrackViewHolder.OnItemClickListener) : RecyclerView.Adapter<TrackViewHolder> () {


    var tracks = ArrayList<Track>()

//    interface OnItemClickListener {
//        fun onItemClick(track: Track)
//    }
//    private var listener: OnItemClickListener? = null
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
        holder.itemView.setOnClickListener{
            listener.onClick(tracks[position])
        }
    }

    override fun getItemCount() = tracks.size
}



