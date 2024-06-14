package com.example.playlistmaker.di

import com.example.playlistmaker.ui.media.favoritetracks.FavoriteTracksViewModel
import com.example.playlistmaker.ui.media.playlists.PlaylistsViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.new_playlist.NewPlaylistViewModel
import com.example.playlistmaker.ui.search.SearchViewModel
import com.example.playlistmaker.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {(track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel{
        FavoriteTracksViewModel(get())
    }

    viewModel{
        PlaylistsViewModel(get())
    }

    viewModel{
        NewPlaylistViewModel(get())
    }
}