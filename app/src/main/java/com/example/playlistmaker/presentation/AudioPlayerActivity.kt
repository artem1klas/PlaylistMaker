package com.example.playlistmaker.presentation

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.Track
import com.google.gson.Gson
import java.util.Locale

const val SELECTED_TRACK = "selected_track"
private const val DELAY_MILLIS = 300L

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private var audioPlayerHolder: AudioPlayerHolder? = null
    private var mainThreadHandler: Handler? = null

    private var playButton: ImageView? = null
    private var addButton: ImageView? = null
    private var likeButton: ImageView? = null
    private var backButton: Button? = null
    private var timeView: TextView? = null

    private var mediaPlayer = Creator.provideAudioPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_player)
        audioPlayerHolder = AudioPlayerHolder(this)
        track = getTrack(intent.getStringExtra(SELECTED_TRACK))
        url = track.previewUrl
        mainThreadHandler = Handler(Looper.getMainLooper())
        audioPlayerHolder!!.bind(track)

        playButton = findViewById(R.id.playButton)
        addButton = findViewById(R.id.addButton)
        likeButton = findViewById(R.id.likeButton)
        backButton = findViewById(R.id.arrow_back)
        timeView = findViewById(R.id.currentTrackTime)

        backButton?.setOnClickListener {
            finish()
        }

        addButton?.setOnClickListener {
            addButton?.setImageResource(R.drawable.player_add_clicked)
        }

        playButton?.setOnClickListener {
            playbackControl()
        }

        likeButton?.setOnClickListener {
            likeButton?.setImageResource(R.drawable.player_like_clicked)
        }

        preparePlayer()
    }

    private val updateTimeViewRunnable = object : Runnable {
            override fun run() {
                timeView?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
                mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
            }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton?.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton?.setImageResource(R.drawable.player_play)
            timeView?.text = resources.getString(R.string.time_null)
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacks(updateTimeViewRunnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton?.setImageResource(R.drawable.player_play_clicked)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(updateTimeViewRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton?.setImageResource(R.drawable.player_play)
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(updateTimeViewRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacks(updateTimeViewRunnable)
    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}


