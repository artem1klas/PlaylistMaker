package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.data.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(PlayerRepositoryImpl())
    }

    private fun getSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideSearchInteractor(context: Context): SearchInteractorImpl {
        return SearchInteractorImpl(getSearchRepository(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(HistoryRepositoryImpl(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(SettingsRepositoryImpl(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(ExternalNavigatorImpl(context))
    }
}