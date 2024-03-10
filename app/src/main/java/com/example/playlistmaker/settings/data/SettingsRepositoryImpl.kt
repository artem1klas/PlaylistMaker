package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.settings.domain.SettingsRepository


//private const val APP_THEME_SHARED_PREFENCES = "dark_theme_preferences"
private const val IS_DARK_THEME_KEY = "key_for_dark_theme"

class SettingsRepositoryImpl(
//    context: Context,
    val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(IS_DARK_THEME_KEY, false)
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPreferences.edit()
            .putBoolean(IS_DARK_THEME_KEY, settings)
            .apply()
    }

}