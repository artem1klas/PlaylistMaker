package com.example.playlistmaker.di

import com.example.playlistmaker.data.impl.media.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.converters.TrackDtoConvertor
import com.example.playlistmaker.data.impl.media.NewPlaylistRepositoryImpl
import com.example.playlistmaker.domain.api_impl.media.FavoriteTrackRepository
import com.example.playlistmaker.data.impl.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api_impl.player.PlayerRepository
import com.example.playlistmaker.data.impl.search.HistoryRepositoryImpl
import com.example.playlistmaker.data.impl.search.SearchRepositoryImpl
import com.example.playlistmaker.domain.api_impl.search.HistoryRepository
import com.example.playlistmaker.domain.api_impl.search.SearchRepository
import com.example.playlistmaker.data.impl.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.api_impl.settings.SettingsRepository
import com.example.playlistmaker.data.impl.settings.ExternalNavigatorImpl
import com.example.playlistmaker.domain.api_impl.media.NewPlaylistRepository
import com.example.playlistmaker.domain.api_impl.settings.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory {
        TrackDtoConvertor()
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(androidContext(), get(), get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidContext())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory {
        TrackDbConvertor()
    }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }

    single<NewPlaylistRepository> {
        NewPlaylistRepositoryImpl()
    }

}