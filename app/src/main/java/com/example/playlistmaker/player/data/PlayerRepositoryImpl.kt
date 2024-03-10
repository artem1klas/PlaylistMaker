package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private var player: MediaPlayer? = null

    override fun setDataSource(url: String) {
        player = MediaPlayer()
        player?.setDataSource(url)
    }
    override fun prepareAsync() {
//        player = MediaPlayer()
        player?.prepareAsync()
    }

    override fun start() {
        player?.start()
    }

    override fun pause() {
        player?.pause()
    }

    override fun release() {
        player?.release()
        player = null
    }

    override fun isPlaying(): Boolean {
        return player?.isPlaying() ?: false
    }

    override fun getCurrentPosition(): Int {
        return player?.getCurrentPosition() ?: 0
    }

    override fun seekTo(position: Int) {
        player?.seekTo(position)
    }

    override fun setOnPreparedListener(onPreparedListener: () -> Unit) {
        player?.setOnPreparedListener { onPreparedListener() }
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        player?.setOnCompletionListener { onCompletionListener() }
    }
}