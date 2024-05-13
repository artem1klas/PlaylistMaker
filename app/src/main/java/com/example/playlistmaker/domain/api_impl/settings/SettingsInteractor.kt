package com.example.playlistmaker.domain.api_impl.settings

interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(setting: Boolean)
}