package com.example.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel by viewModel<PlaylistViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: Playlist
    private val tracks = mutableListOf<Track>()


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
        viewModel.getPlaylist(requireArguments().getInt(SELECTED_PLAYLIST))

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    fun render(state: PlaylistState){
        when(state){
            is PlaylistState.Loading -> {
                binding.mainGroup.isVisible = false
                binding.albumBottomSheet.isVisible = false
                binding.progressBar.isVisible = true
            }
            is PlaylistState.Content -> {
                playlist = state.playlist
                bind(playlist)
                binding.mainGroup.isVisible = true
                binding.albumBottomSheet.isVisible = true
                binding.progressBar.isVisible = false
            }
        }
    }

    fun bind(playlist: Playlist){
        Glide.with(requireContext())
            .load(playlist.uri)
            .placeholder(R.drawable.image_placeholdertrack)
            .transform(CenterCrop())
            .into(binding.image)
        binding.title.text = playlist.namePlaylist
        binding.description.text = playlist.descriptionPlaylist
        binding.durationAndCount.text = playlist.size.toString()
        tracks.clear()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SELECTED_PLAYLIST = "selected_playlist"

        fun createArgs(albumId: Int): Bundle = bundleOf(SELECTED_PLAYLIST to albumId)
    }

}