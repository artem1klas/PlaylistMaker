package com.example.playlistmaker.domain.api_impl.settings

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink()
    fun openEmail()
}