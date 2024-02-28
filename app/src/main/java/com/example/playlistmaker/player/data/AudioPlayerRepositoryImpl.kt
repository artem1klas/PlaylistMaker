package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.AudioPlayerReposotory

class AudioPlayerRepositoryImpl() : AudioPlayerReposotory {
    val player = MediaPlayer()

    override fun prepareAsync() {
        player.prepareAsync()
    }

    override fun start() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun release() {
        player.release()
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return player.getCurrentPosition()
    }

    override fun seekTo(position: Int) {
        player.seekTo(position)
    }

    override fun setDataSource(url: String) {
        player.setDataSource(url)
    }

    override fun setOnPreparedListener(onPreparedListener: () -> Unit) {
        player.setOnPreparedListener { onPreparedListener() }
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        player.setOnCompletionListener { onCompletionListener() }
    }
}
