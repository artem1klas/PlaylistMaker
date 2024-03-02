package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson


class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    private lateinit var track: Track
    private var audioPlayerHolder: PlayerHolder? = null
    private lateinit var binding: MediaPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrack(intent.getStringExtra(SELECTED_TRACK))

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.previewUrl))[PlayerViewModel::class.java]
        viewModel.observeState().observe(this) {
            when(it) {
                is PlayerState.Default -> {
                    viewModel.preparePlayer()
                }
                is PlayerState.Prepared -> {
                    preparePlayer()
                }
                is PlayerState.Playing -> {
                    binding.currentTrackTime.text = it.time
                }
                is PlayerState.Paused -> {
                    binding.currentTrackTime.text = it.time
                }
            }
        }

        audioPlayerHolder = PlayerHolder(binding, this)
        audioPlayerHolder!!.bind(track)
        binding.currentTrackTime.text = CURENT_TRACK_TIME

        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.addButton.setOnClickListener {
            binding.addButton.setImageResource(R.drawable.player_add_clicked)
        }

        binding.playButton.setOnClickListener {
            playbackControl()
        }

        binding.likeButton.setOnClickListener {
            binding.likeButton.setImageResource(R.drawable.player_like_clicked)
        }
    }

    private fun preparePlayer() {
        binding.playButton.setImageResource(R.drawable.player_play)
        binding.currentTrackTime.text = resources.getString(R.string.time_null)
        binding.playButton.isEnabled = true
    }

    private fun startPlayer() {
        viewModel.startPlayer()
        binding.playButton.setImageResource(R.drawable.player_play_clicked)
    }

    private fun pausePlayer() {
        viewModel.pausePlayer()
        binding.playButton.setImageResource(R.drawable.player_play)
    }

    private fun playbackControl() {
        when (viewModel.observeState().value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared -> {
                startPlayer()
            }
            is PlayerState.Paused -> {
                startPlayer()
            }
            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

    companion object {
        const val CURENT_TRACK_TIME = "00:00"
        const val SELECTED_TRACK = "selected_track"
    }
}


