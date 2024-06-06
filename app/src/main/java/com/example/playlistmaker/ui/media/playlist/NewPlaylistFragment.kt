package com.example.playlistmaker.ui.media.playlist

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
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.InvocationTargetException

class NewPlaylistFragment : Fragment() {
//    companion object {
//        fun newInstance() = NewPlaylistFragment()
//    }

    private val viewModel by viewModel<NewPlaylistViewModel>()


    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var uri: Uri? = null


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

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.createPlaylist.isEnabled = false


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    this.uri = uri
                    binding.newPlayListImage.setImageURI(uri)
                    //       saveImageToPrivateStorage(uri)
                }
            }

        binding.newPlayListImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.namePlaylist.addTextChangedListener(textWatcher)

        binding.createPlaylist.setOnClickListener {
            try{
                viewModel.createNewPlaylist(
                    binding.namePlaylist.text.toString(),
                    binding.descriptionPlaylist.text.toString(),
                    uri
                )
            }catch (e: InvocationTargetException){
                Toast.makeText(context, e.cause.toString(), Toast.LENGTH_LONG).show()
            }
            Toast.makeText(context, "Плейлист ${binding.namePlaylist.text.toString()} создан", Toast.LENGTH_LONG).show()
        }


    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!p0.isNullOrEmpty()) {
                binding.createPlaylist.isEnabled = true
            } else {
                binding.createPlaylist.isEnabled = false
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}