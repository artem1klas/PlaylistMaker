package com.example.playlistmaker.di

import com.example.playlistmaker.domain.api_impl.media.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api_impl.media.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.api_impl.media.NewPlaylistInteractor
import com.example.playlistmaker.domain.api_impl.media.NewPlaylistInteractorImpl
import com.example.playlistmaker.domain.api_impl.player.PlayerInteractor
import com.example.playlistmaker.domain.api_impl.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.api_impl.search.HistoryInteractor
import com.example.playlistmaker.domain.api_impl.search.SearchInteractor
import com.example.playlistmaker.domain.api_impl.search.HistoryInteractorImpl
import com.example.playlistmaker.domain.api_impl.search.SearchInteractorImpl
import com.example.playlistmaker.domain.api_impl.settings.SettingsInteractor
import com.example.playlistmaker.domain.api_impl.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.api_impl.settings.SharingInteractor
import com.example.playlistmaker.domain.api_impl.settings.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<SearchInteractor>{
        SearchInteractorImpl(get())
    }

    factory<HistoryInteractor>{
        HistoryInteractorImpl(get())
    }

    factory<PlayerInteractor>{
        PlayerInteractorImpl(get())
    }

    factory<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor>{
        SharingInteractorImpl(get())
    }

    factory<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    factory<NewPlaylistInteractor>{
        NewPlaylistInteractorImpl(get())
    }
}