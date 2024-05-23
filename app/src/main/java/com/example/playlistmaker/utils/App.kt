package com.example.playlistmaker.utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class App : Application() {

    private var isDarkTheme = false
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        isDarkTheme = getSharedPreferences(
            PLAYLISTMAKER_SHARED_PREFENCES,
            Context.MODE_PRIVATE
        ).getBoolean(IS_DARK_THEME_KEY, false)
        setDarkTheme(isDarkTheme)
    }
    companion object {
        private const val PLAYLISTMAKER_SHARED_PREFENCES = "playlistmaker_shared_preferences"
        private const val IS_DARK_THEME_KEY = "key_for_dark_theme"
        fun setDarkTheme(isDarkTheme: Boolean) {
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }
}
