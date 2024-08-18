package com.example.playlistmaker.ui.media.edit_playlist

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.new_playlist.CreatePlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: CreatePlaylistFragment() {
    override val viewModel by viewModel<EditPlaylistViewModel>()

    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = "Редактировать"
        binding.createPlaylist.text = "Сохранить"

    }

}