package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PLAYLISTMAKER_SHARED_PREFENCES = "playlistmaker_shared_preferences"

val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().
            create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single {
        androidContext().getSharedPreferences(
            PLAYLISTMAKER_SHARED_PREFENCES,
            Context.MODE_PRIVATE
        )
    }

    factory {
        Gson()
    }

    single {
        MediaPlayer()
    }

}