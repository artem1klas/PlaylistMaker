package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

     internal var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val  sharedPrefs = getSharedPreferences(APP_THEME_SHARED_PREFENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(IS_DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}