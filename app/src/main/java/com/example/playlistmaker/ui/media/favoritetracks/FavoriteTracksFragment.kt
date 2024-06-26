package com.example.playlistmaker.ui.media.favoritetracks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.adapters.track.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks) { track ->
        onTrackClickDebounce(track)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.windowTrackList.layoutManager = LinearLayoutManager(requireContext())
        binding.windowTrackList.adapter = adapter

        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_mediaFragment_to_playerFragment,
                PlayerFragment.createArgs(trackId = Gson().toJson(track))
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        fun newInstance() = FavoriteTracksFragment()
    }


}