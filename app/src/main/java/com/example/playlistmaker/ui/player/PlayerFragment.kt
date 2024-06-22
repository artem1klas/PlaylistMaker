package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.adapters.playlist.PlaylistBottomsheetAdapter
import com.example.playlistmaker.ui.media.new_playlist.CreatePlaylistFragment
import com.example.playlistmaker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private lateinit var onPlaylistClick: (Playlist) -> Unit
    private val playlists = ArrayList<Playlist>()
    private val adapter = PlaylistBottomsheetAdapter(playlists) { playlist ->
        onPlaylistClick(playlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
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

        binding.windowPlaylistsBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        binding.windowPlaylistsBottomSheet.adapter = adapter

        Glide.with(binding.root)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.image_placeholdertrack)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
            .into(binding.imageTrack)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        viewModel.observeState().observe(viewLifecycleOwner) {
            if (it is PlayerState.ButtomSheet) {
                playlists.clear()
                playlists.addAll(it.playlists.reversed())
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                adapter.notifyDataSetChanged()
            }
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            binding.currentTrackTime.text = it.progress
            if (it.isButtonPlay) {
                binding.playButton.setImageResource(R.drawable.player_play)
            } else {
                binding.playButton.setImageResource(R.drawable.player_play_clicked)
            }
            if (it.isLiked) {
                binding.likeButton.setImageResource(R.drawable.player_like_clicked)
            } else {
                binding.likeButton.setImageResource(R.drawable.player_like)
            }
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

        binding.createPlaylistBottomSheet.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_newPlaylistFragment,
                CreatePlaylistFragment.createArgs(trackId = Gson().toJson(track))
            )
        }

        binding.addButton.setOnClickListener {
            viewModel.addButtonClicked()
        }

        onPlaylistClick = { playlist ->
            if (viewModel.addTrackToPlaylist(playlist)) {
                Toast.makeText(
                    context,
                    getString(R.string.added_in_playlist, playlist.namePlaylist),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    context,
                    getString(
                        R.string.track_has_already_been_added_to_playlist,
                        playlist.namePlaylist
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onResume() {
        super.onResume()
        BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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