package com.example.playlistmaker.settings.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.settings.domain.SettingsRepository


const val APP_THEME_SHARED_PREFENCES = "dark_theme_preferences"
const val IS_DARK_THEME_KEY = "key_for_dark_theme"

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    val sharedPrefs = context.getSharedPreferences(
        APP_THEME_SHARED_PREFENCES,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun getThemeSettings(): Boolean {
        return sharedPrefs.getBoolean(IS_DARK_THEME_KEY, false)
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPrefs.edit()
            .putBoolean(IS_DARK_THEME_KEY, settings)
            .apply()
    }

}