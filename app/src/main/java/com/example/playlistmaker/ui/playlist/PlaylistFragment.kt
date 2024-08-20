package com.example.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.adapters.track.TrackAdapter
import com.example.playlistmaker.ui.media.edit_playlist.EditPlaylistFragment
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.utils.debounce
import com.example.playlistmaker.utils.declineMinute
import com.example.playlistmaker.utils.declineTrack
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel by viewModel<PlaylistViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: Playlist
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks) { track ->
        onTrackClickDebounce(track)
    }
    private lateinit var onTrackClickDebounce: (Track) -> Unit


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }


        binding.tracks.adapter = adapter
        binding.tracks.layoutManager = LinearLayoutManager(requireContext())
        adapter.onClickLong = {
            showDeleteTrackDialog(playlist, it.trackId)
        }

        viewModel.fillData(requireArguments().getInt(SELECTED_PLAYLIST))

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_playerFragment,
                PlayerFragment.createArgs(trackId = Gson().toJson(track))
            )
        }

        binding.share.setOnClickListener {
            if (tracks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.sharePlaylist(playlist.toText())
            }
        }

        val bottomSheetBehaviorTracks = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehaviorTracks.peekHeight =
            (resources.displayMetrics.heightPixels * 0.25).toInt()
        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }


        bottomSheetBehaviorMenu.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(p0: View, p1: Int) {
                    when (p1) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            bottomSheetBehaviorTracks.isHideable = false
                            binding.tracksBottomSheet.visibility = View.VISIBLE
                            binding.overlay.isVisible = false
                        }

                        else -> {
                            bottomSheetBehaviorTracks.isHideable = true
                            bottomSheetBehaviorTracks.state = BottomSheetBehavior.STATE_HIDDEN
                            binding.tracksBottomSheet.visibility = View.GONE
                            binding.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(p0: View, p1: Float) {}

            }
        )

        binding.menu.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.menuShare.setOnClickListener {
            if (tracks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.sharePlaylist(playlist.toText())
            }
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuDelete.setOnClickListener {
            showDeletePlaylistDialog(playlist)
        }

        binding.menuEdit.setOnClickListener {
            val jsonPlaylist = Gson().toJson(playlist)
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(jsonPlaylist)
            )
        }
    }

    fun Playlist.toText(): String {
        val stringBuilder = StringBuilder()
            .append(namePlaylist)
            .append("\n")
            .append(if (descriptionPlaylist.isNotBlank()) "$descriptionPlaylist \n" else "")
            .append("${size} ${declineTrack(requireContext(), size)}")
            .append("\n")

        for ((index, track) in tracks.withIndex()) {
            stringBuilder.append("${index + 1}.${track.artistName} - ${track.trackName} (${track.trackTimeMillis})\n")
        }
        return stringBuilder.toString()
    }

    private fun showDeletePlaylistDialog(playlist: Playlist) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.do_you_want_delete_playlist, playlist.namePlaylist))
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist(playlist)
                findNavController().navigateUp()

            }
            .show()
    }

    private fun showDeleteTrackDialog(playlist: Playlist, trackId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Хотите удалить трек?")
            .setNegativeButton("Нет") { _, _ -> }
            .setPositiveButton("Да") { _, _ ->

                val position = tracks.indexOfFirst {
                    it.trackId == trackId
                }
                if (position != -1) {
                    tracks.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
                binding.durationAndCount.text = getDurationAndCount(tracks)
                viewModel.deleteTrackFromPlaylist(playlist, trackId)
            }
            .show()
    }

    fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Loading -> {
                binding.mainGroup.isVisible = false
                binding.tracksBottomSheet.isVisible = false
                binding.progressBar.isVisible = true
            }
            is PlaylistState.Content -> {
                playlist = state.playlist
                tracks.clear()
                tracks.addAll(state.tracks)
                bind(playlist)
                binding.mainGroup.isVisible = true
                binding.tracksBottomSheet.isVisible = true
                binding.progressBar.isVisible = false
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun bind(playlist: Playlist) {
        Glide.with(requireContext())
            .load(playlist.uri)
            .placeholder(R.drawable.image_placeholdertrack)
            .transform(CenterCrop())
            .into(binding.image)
        binding.title.text = playlist.namePlaylist
        binding.description.text = playlist.descriptionPlaylist
        binding.durationAndCount.text = getDurationAndCount(tracks)

        Glide.with(requireContext())
            .load(playlist.uri)
            .placeholder(R.drawable.image_placeholdertrack)
            .transform(CenterCrop())
            .into(binding.menuPlaylistImage)
        binding.menuPlaylistName.text = playlist.namePlaylist
        binding.menuPlaylistSize.text =
            "${playlist.size} ${declineTrack(requireContext(), playlist.size)}"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDurationAndCount(tracks: List<Track>): String {
        val totalDuration = tracks.map { track ->
            val time = track.trackTimeMillis.split(":").map { it.toInt() }
            time[0] * 60 + time[1]
        }.sum() / 60

        val tracksCount = tracks.size
        return "$totalDuration ${declineMinute(requireContext(), totalDuration)} • $tracksCount ${
            declineTrack(
                requireContext(),
                tracksCount
            )
        } "
    }

    companion object {
        const val SELECTED_PLAYLIST = "selected_playlist"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        fun createArgs(albumId: Int): Bundle = bundleOf(SELECTED_PLAYLIST to albumId)
    }

}