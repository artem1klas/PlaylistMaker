package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaPlayerBinding
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerActivity : AppCompatActivity() {


    private lateinit var track: Track
    private lateinit var binding: MediaPlayerBinding

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track?.previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrack(intent.getStringExtra(SELECTED_TRACK))

        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTimeMillisValue.text = track.trackTimeMillis
        binding.albumValue.text = track.collectionName
        binding.yearValue.text = track.releaseDate
        binding.genreValue.text = track.primaryGenreName
        binding.countryValue.text = track.country

        Glide.with(binding.root)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.image_placeholdertrack)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, applicationContext)))
            .into(binding.imageTrack)

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