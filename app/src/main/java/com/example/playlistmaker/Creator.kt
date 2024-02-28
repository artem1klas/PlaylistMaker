package com.example.playlistmaker

import com.example.playlistmaker.data.AudioPlayerRepositoryImpl
import com.example.playlistmaker.domain.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.AudioPlayerInteractor

object Creator {
    fun provideAudioPlayer(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(AudioPlayerRepositoryImpl())
    }
}