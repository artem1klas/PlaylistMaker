package com.example.playlistmaker.domain.api_impl.player

interface PlayerInteractor {

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