package com.example.playlistmaker.domain.api_impl.settings

interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(setting: Boolean)
}