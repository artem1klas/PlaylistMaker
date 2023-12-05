package com.example.playlistmaker

import com.example.playlistmaker.data.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.AudioPlayerInteractor

object Creator {
    fun provideAudioPlayer(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl()
    }
}