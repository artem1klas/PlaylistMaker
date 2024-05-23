package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.network.ITunesApiService
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PLAYLISTMAKER_SHARED_PREFENCES = "playlistmaker_shared_preferences"

private const val BASE_URL = "https://itunes.apple.com"
val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().
            create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    factory {
        Gson()
    }

    single {
        androidContext().getSharedPreferences(
            PLAYLISTMAKER_SHARED_PREFENCES,
            Context.MODE_PRIVATE
        )
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

}