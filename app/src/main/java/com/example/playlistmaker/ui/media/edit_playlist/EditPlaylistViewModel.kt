package com.example.playlistmaker.ui.media.edit_playlist

import com.example.playlistmaker.domain.api_impl.media.playlist.PlaylistInteractor
import com.example.playlistmaker.ui.media.new_playlist.CreatePlaylistViewModel

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): CreatePlaylistViewModel(playlistInteractor) {
}