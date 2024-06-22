package com.example.playlistmaker.ui.media.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.new_playlist.CreatePlaylistFragment
import com.example.playlistmaker.ui.adapters.playlist.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {


    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var onPlaylistClick: (Playlist) -> Unit
    private val playlists = ArrayList<Playlist>()
    private val adapter = PlaylistAdapter(playlists){ playlists ->
        onPlaylistClick(playlists)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment,
                    CreatePlaylistFragment.createArgs("-1")
                )
        }

        binding.playlistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsList.adapter = adapter

        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        onPlaylistClick = {}
    }

    @SuppressLint("NotifyDataSetChanged")
    fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> {
                binding.empty.isVisible = true
                binding.playlistsList.isVisible = false
                binding.windowProgressBar.isVisible = false
            }

            is PlaylistsState.Content -> {
                playlists.clear()
                playlists.addAll(state.playlists.reversed())
                binding.empty.isVisible = false
                binding.playlistsList.isVisible = true
                binding.windowProgressBar.isVisible = false
            }

            is PlaylistsState.Loading -> {
                binding.empty.isVisible = false
                binding.playlistsList.isVisible = false
                binding.windowProgressBar.isVisible = true
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}








