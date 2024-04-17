package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerFragment : Fragment() {

    companion object {
        const val CURENT_TRACK_TIME = "00:00"
        const val SELECTED_TRACK = "selected_track"

        fun createArgs(trackId: String): Bundle = bundleOf(SELECTED_TRACK to trackId)

    }


    private lateinit var track: Track
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track?.previewUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = getTrack(requireArguments().getString(SELECTED_TRACK))

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
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
            .into(binding.imageTrack)

        viewModel.observeState().observe(viewLifecycleOwner) {
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

        binding.addButton.setOnClickListener {
            binding.addButton.setImageResource(R.drawable.player_add_clicked)
        }

        binding.playButton.setOnClickListener {
            playbackControl()
        }

        binding.likeButton.setOnClickListener {
            binding.likeButton.setImageResource(R.drawable.player_like_clicked)
        }

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)



}