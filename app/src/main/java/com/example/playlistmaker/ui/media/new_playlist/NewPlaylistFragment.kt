package com.example.playlistmaker.ui.media.new_playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.utils.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private val viewModel by viewModel<NewPlaylistViewModel>()


    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var uri: Uri? = null

    private var track: Track? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = getTrack(requireArguments().getString(SELECTED_TRACK))

        binding.arrowBack.setOnClickListener {
            if (uri == null
                && binding.namePlaylist.text.toString().isNullOrEmpty()
                && binding.descriptionPlaylist.text.toString().isNullOrEmpty()
            ) {
                findNavController().navigateUp()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.finish_creating_a_playlist))
                    .setNeutralButton(getString(R.string.cancel)){ _, _ ->
                    }
                    .setPositiveButton(getString(R.string.complete)){ _, _ ->
                        findNavController().navigateUp()
                    }
                    .show()

            }


        }


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    this.uri = uri
                    Glide.with(requireContext())
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8f, requireContext())))
                        .into(binding.newPlayListImage)
                }
            }

        binding.newPlayListImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.namePlaylist.addTextChangedListener(textWatcher)

        binding.createPlaylist.setOnClickListener {
            Toast.makeText(
                context,
                "Плейлист ${binding.namePlaylist.text} создан",
                Toast.LENGTH_LONG
            ).show()
            val trackIds = if (track != null) mutableListOf(track!!.trackId) else mutableListOf()

            viewModel.createNewPlaylist(
                binding.namePlaylist.text.toString(),
                binding.descriptionPlaylist.text.toString(),
                uri.toString(),
                trackIds
            )
            if (uri != null) {
                saveImageToPrivateStorage(binding.namePlaylist.text.toString(), uri!!)
            }
            findNavController().navigateUp()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!p0.isNullOrEmpty()) {
                binding.createPlaylist.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                binding.createPlaylist.isEnabled = true
            } else {
                binding.createPlaylist.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                binding.createPlaylist.isEnabled = false
            }
        }

        override fun afterTextChanged(p0: Editable?) {}

    }

    fun saveImageToPrivateStorage(name: String, uri: Uri) {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlistImages"
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, name)

        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SELECTED_TRACK = "selected_track"
        fun createArgs(trackId: String): Bundle = bundleOf(PlayerFragment.SELECTED_TRACK to trackId)

    }
}