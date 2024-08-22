package com.example.playlistmaker.ui.media.edit_playlist

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.new_playlist.CreatePlaylistFragment
import com.example.playlistmaker.utils.dpToPx
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URI

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistViewModel>()
    private lateinit var playlist: Playlist
    private var uriEdit: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = getString(R.string.edit)
        binding.createPlaylist.text = getString(R.string.save)

        playlist =
            Gson().fromJson(requireArguments().getString(EDITABLE_PLAYLIST), Playlist::class.java)

        binding.namePlaylist.setText(playlist.namePlaylist)
        binding.descriptionPlaylist.setText(playlist.descriptionPlaylist)
        if (playlist.uri.isNotBlank()){
            uriEdit = playlist.uri.toUri()
        }

        Glide.with(requireContext())
            .load(playlist.uri.toUri())
            .placeholder(R.drawable.ic_new_playlist)
            .transform(CenterCrop(), RoundedCorners(dpToPx(8f, requireContext())))
            .into(binding.newPlayListImage)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    uriEdit = uri

                    Glide.with(requireContext())
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8f, requireContext())))
                        .into(binding.newPlayListImage)
                }
            }

        binding.newPlayListImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylist.setOnClickListener {
            if (uriEdit != playlist.uri.toUri()) {
                uriEdit = saveImageToPrivateStorage(uriEdit!!)
            }
            val newPlaylist = Playlist(
                id = playlist.id,
                namePlaylist = binding.namePlaylist.text.toString(),
                descriptionPlaylist = binding.descriptionPlaylist.text.toString(),
                uri = uriEdit.toString(),
                trackIds = playlist.trackIds,
                size = playlist.size
            )
            if(newPlaylist != playlist) {
                viewModel.updatePlaylist(newPlaylist)
            }
            findNavController().navigateUp()

        }
        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    companion object {
        const val EDITABLE_PLAYLIST = "editable_playlist"
        fun createArgs(trackId: String): Bundle =
            bundleOf(EDITABLE_PLAYLIST to trackId)

    }

}