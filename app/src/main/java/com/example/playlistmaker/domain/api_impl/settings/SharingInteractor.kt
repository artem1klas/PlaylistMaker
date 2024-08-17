package com.example.playlistmaker.domain.api_impl.settings

interface SharingInteractor {
    fun shareApp()
    fun shareText(text: String)

    fun openTerms()
    fun openSupport()
}