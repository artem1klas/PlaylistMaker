package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayerInteractor
import java.util.Locale

class PlayerViewModel(
    private val url: String,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    val handler = Handler(Looper.getMainLooper())

    private val playerLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observeState(): LiveData<PlayerState> = playerLiveData

    fun preparePlayer() {
        playerInteractor.setDataSource(url)
        playerInteractor.prepareAsync()
        playerInteractor.setOnPreparedListener {
            playerLiveData.postValue(PlayerState.Prepared)
        }
        playerInteractor.setOnCompletionListener {
            handler.removeCallbacks(updateTimeViewRunnable)
        }
    }

    fun startPlayer() {
        playerInteractor.start()
        playerLiveData.postValue(PlayerState.Playing(getCurrentPosition()))
        handler.post(updateTimeViewRunnable)
    }

    fun pausePlayer() {
        playerInteractor.pause()
        playerLiveData.postValue(PlayerState.Paused(getCurrentPosition()))
        handler.removeCallbacks(updateTimeViewRunnable)
    }

    private val updateTimeViewRunnable = object : Runnable {
        override fun run() {
            playerLiveData.postValue(PlayerState.Playing(getCurrentPosition()))
            handler.postDelayed(this, DELAY_MILLIS)
        }
    }

    private fun getCurrentPosition(): String {
        return dateFormat.format(playerInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }

}