package com.example.playlistmaker.player.domain

interface AudioPlayerReposotory {

    fun prepareAsync()

    fun start()

    fun pause()

    fun release()

    fun isPlaying(): Boolean

    fun getCurrentPosition(): Int

    fun seekTo(position: Int)

    fun setDataSource(url: String)

    fun setOnPreparedListener(onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

}
