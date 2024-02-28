package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sharingInteractor = Creator.provideSharingInteractor(application)
    private val settingsInteractor = Creator.provideSettingsInteractor(application)

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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

}