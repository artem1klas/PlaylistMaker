package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.favoritetracks.FavoriteTracksViewModel
import com.example.playlistmaker.media.ui.playlist.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {(url: String) ->
        PlayerViewModel(url, get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel{
        FavoriteTracksViewModel(get())
    }

    viewModel{
        PlaylistsViewModel()
    }
}