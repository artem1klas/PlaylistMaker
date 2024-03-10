package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.SettingsRepository

private const val PLAYLISTMAKER_SHARED_PREFENCES = "playlistmaker_shared_preferences"
private const val IS_DARK_THEME_KEY = "key_for_dark_theme"

class SettingsRepositoryImpl(
   context: Context
) : SettingsRepository {

    val sharedPreferences = context.getSharedPreferences(
    PLAYLISTMAKER_SHARED_PREFENCES,
    Context.MODE_PRIVATE
    )

    override fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(IS_DARK_THEME_KEY, false)
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPreferences.edit()
            .putBoolean(IS_DARK_THEME_KEY, settings)
            .apply()
    }

}