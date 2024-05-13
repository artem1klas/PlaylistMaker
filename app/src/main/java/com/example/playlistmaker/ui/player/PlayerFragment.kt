package com.example.playlistmaker.ui.player

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
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.dpToPx
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerFragment : Fragment() {

    private lateinit var track: Track
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
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
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            binding.currentTrackTime.text = it.progress
            if (it.isButtonPlay) {
                binding.playButton.setImageResource(R.drawable.player_play)
            } else {
                binding.playButton.setImageResource(R.drawable.player_play_clicked)
            }
            if (it.isLiked){
                binding.likeButton.setImageResource(R.drawable.player_like_clicked)
            } else {
                binding.likeButton.setImageResource(R.drawable.player_like)
            }
        }

        binding.addButton.setOnClickListener {
            binding.addButton.setImageResource(R.drawable.player_add_clicked)
        }

        binding.likeButton.setOnClickListener {
            viewModel.likeButtonClicked()
        }

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            viewModel.playButtonClicked()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

    companion object {
        const val SELECTED_TRACK = "selected_track"

        fun createArgs(trackId: String): Bundle = bundleOf(SELECTED_TRACK to trackId)

    }

}