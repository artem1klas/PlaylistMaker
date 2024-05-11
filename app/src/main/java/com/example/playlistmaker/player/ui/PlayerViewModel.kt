package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val url: String,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default(getCurrentPosition()))
    fun observeState(): LiveData<PlayerState> = playerState

    init {
        preparePlayer()
    }

    fun preparePlayer() {
        playerInteractor.setDataSource(url)
        playerInteractor.prepareAsync()
        playerInteractor.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared(getCurrentPosition()))
        }
        playerInteractor.setOnCompletionListener {
           playerState.postValue(PlayerState.Default(getCurrentPosition()))
        }
    }

    fun playButtonClicked() {
        when (playerState.value) {

            is PlayerState.Prepared -> {
                startPlayer()
            }

            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    fun startPlayer() {
        playerInteractor.start()
        playerState.postValue(PlayerState.Playing(getCurrentPosition()))
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()){
                delay(DELAY_MILLIS)
                playerState.postValue(PlayerState.Playing(getCurrentPosition()))
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPosition()))
    }

    private fun getCurrentPosition(): String {
        return dateFormat.format(playerInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        playerState.value = PlayerState.Default(getCurrentPosition())
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }

}