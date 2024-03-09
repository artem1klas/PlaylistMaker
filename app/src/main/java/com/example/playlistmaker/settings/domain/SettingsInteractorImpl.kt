package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()
    }
    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(settings)
    }
}