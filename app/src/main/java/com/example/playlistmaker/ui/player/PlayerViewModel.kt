package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.favorite_track.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api_impl.media.playlist.NewPlaylistInteractor
import com.example.playlistmaker.domain.api_impl.player.PlayerInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val newPlaylistInteractor: NewPlaylistInteractor
) : ViewModel() {

    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    private var trackIsLicked = false

    private var timerJob: Job? = null

    private val playerState =
        MutableLiveData<PlayerState>(PlayerState.Default(getCurrentPosition(), trackIsLicked))

    fun observeState(): LiveData<PlayerState> = playerState

    init {
        preparePlayer()

        viewModelScope.launch {
            favoriteTrackInteractor
                .getIdFavoriteTracks()
                .collect { idTracks ->
                    if (idTracks.contains(track.trackId)) {
                        trackIsLicked = true
                    }
                }

        }
    }

    fun preparePlayer() {
        playerInteractor.setDataSource(track.previewUrl)
        playerInteractor.prepareAsync()

        playerInteractor.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared(getCurrentPosition(), trackIsLicked))
        }
        playerInteractor.setOnCompletionListener {
            timerJob?.cancel()
            preparePlayer()
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

    fun likeButtonClicked() {

        viewModelScope.launch {
            if (trackIsLicked) {
                favoriteTrackInteractor.removeTrack(track)
            } else {
                favoriteTrackInteractor.addTrack(track)
            }
        }

        playerState.postValue(playerState.value?.apply {
            isLiked = !trackIsLicked
        })
        trackIsLicked = !trackIsLicked
    }

    fun addButtonClicked(): List<Playlist>{
        var playlist = mutableListOf<Playlist>()
        viewModelScope.launch {
            newPlaylistInteractor.getPlaylists().collect{
                playlist = it.toMutableList()
            }
        }
        return playlist
    }

    fun startPlayer() {
        playerInteractor.start()
        playerState.postValue(PlayerState.Playing(getCurrentPosition(), trackIsLicked))
        startTimer()
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(DELAY_MILLIS)
                playerState.postValue(PlayerState.Playing(getCurrentPosition(), trackIsLicked))
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPosition(), trackIsLicked))
    }

    private fun getCurrentPosition(): String {
        return dateFormat.format(playerInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        playerState.postValue(PlayerState.Default(getCurrentPosition(), trackIsLicked))
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }

}