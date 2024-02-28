package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import java.util.Locale

class PlayerViewModel(private val url: String) : ViewModel() {

    private val player = Creator.provideAudioPlayerInteractor()

    val handler = Handler(Looper.getMainLooper())

    private val playerLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = playerLiveData

    fun preparePlayer() {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerLiveData.postValue(PlayerState.Prepared)
        }
        player.setOnCompletionListener {
            handler.removeCallbacks(updateTimeViewRunnable)
        }
    }

    fun startPlayer() {
        player.start()
        playerLiveData.postValue(PlayerState.Playing(getCurrentPosition()))
        handler.post(updateTimeViewRunnable)
    }

    fun pausePlayer() {
        player.pause()
        playerLiveData.postValue(PlayerState.Paused)
        handler.removeCallbacks(updateTimeViewRunnable)
    }

    fun releasePlayer() {
        player.release()
    }

    private val updateTimeViewRunnable = object : Runnable {
        override fun run() {
            playerLiveData.postValue(PlayerState.Playing(getCurrentPosition()))
            handler.postDelayed(this, DELAY_MILLIS)
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.getCurrentPosition())
    }

    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(url)
            }
        }

        private const val DELAY_MILLIS = 300L
    }

}