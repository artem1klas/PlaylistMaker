package com.example.playlistmaker.domain.api_impl.settings

class SettingsInteractorImpl(val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()
    }
    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(settings)
    }
}