package com.example.playlistmaker.ui.media.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.favoritetracks.FavoriteState
import com.example.playlistmaker.ui.media.favoritetracks.FavoriteTracksFragment
import com.example.playlistmaker.ui.media.favoritetracks.FavoriteTracksViewModel
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.ui.track.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private val playlistsViewModel by viewModel<PlaylistsViewModel>()

    private  var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlists = ArrayList<Playlist>()
    private val adapter = PlaylistAdapter(playlists)



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
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }





        binding.playlistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.adapter = adapter

        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}




@SuppressLint("NotifyDataSetChanged")
fun render(state: FavoriteState) {
    when (state) {
        is FavoriteState.Empty -> {
            binding.empty.isVisible = true
            binding.windowTrackList.isVisible = false
            binding.windowProgressBar.isVisible = false
        }

        is FavoriteState.Content -> {
            tracks.clear()
            tracks.addAll(state.tracks.reversed())
            binding.empty.isVisible = false
            binding.windowTrackList.isVisible = true
            binding.windowProgressBar.isVisible = false
        }

        is FavoriteState.Loading -> {
            binding.empty.isVisible = false
            binding.windowTrackList.isVisible = false
            binding.windowProgressBar.isVisible = true
        }
    }
    adapter.notifyDataSetChanged()
}



