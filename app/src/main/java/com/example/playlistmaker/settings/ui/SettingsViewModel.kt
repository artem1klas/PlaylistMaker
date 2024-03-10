package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val switchOnLiveData = MutableLiveData<Boolean>()
    fun observeOnState(): LiveData<Boolean> = switchOnLiveData
    private var isDarkTheme: Boolean = false

    init {
        isDarkTheme = settingsInteractor.getThemeSettings()
        switchOnLiveData.postValue(isDarkTheme)
    }

    fun onSwitchClick(isChecked: Boolean) {
        switchOnLiveData.postValue(isChecked)
        settingsInteractor.updateThemeSetting(isChecked)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }
    fun openTerms() {
        sharingInteractor.openTerms()
    }
    fun openSupport() {
        sharingInteractor.openSupport()
    }

}