package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchInteractor>{
        SearchInteractorImpl(get())
    }

    single<HistoryInteractor>{
        HistoryInteractorImpl(get())
    }

    single<PlayerInteractor>{
        PlayerInteractorImpl(get())
    }

    single<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor>{
        SharingInteractorImpl(get())
    }
}