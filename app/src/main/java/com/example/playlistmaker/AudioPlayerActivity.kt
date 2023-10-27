package com.example.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

const val SELECTED_TRACK = "selected_track"

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private  var audioPlayerHolder: AudioPlayerHolder? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_player)
        audioPlayerHolder = AudioPlayerHolder(this)
        track = getTrack(intent.getStringExtra(SELECTED_TRACK))

        audioPlayerHolder!!.bind(track)

        val backButton: Button = findViewById(R.id.arrow_back)
        val addButton: ImageView = findViewById(R.id.addButton)
        val playButton: ImageView = findViewById(R.id.playButton)
        val likeButton: ImageView = findViewById(R.id.likeButton)

        backButton.setOnClickListener{
            finish()
        }

        addButton.setOnClickListener {
            addButton.setImageResource(R.drawable.player_add_clicked)
        }

        playButton.setOnClickListener{
            playButton.setImageResource(R.drawable.player_play_clicked)
        }

        likeButton.setOnClickListener {
            likeButton.setImageResource(R.drawable.player_like_clicked)
        }

    }


    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)
}