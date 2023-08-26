import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView
    private val trackName: TextView
    private val trackTime: TextView
    private val artistName: TextView

    init {
        image =  itemView.findViewById(R.id.trackImage)
        trackName = itemView.findViewById(R.id.trackName)
        trackTime = itemView.findViewById(R.id.trackTime)
        artistName = itemView.findViewById(R.id.artistName)
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        trackTime.text = track.trackTime
        artistName.text = track.artistName
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(image)
    }
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}