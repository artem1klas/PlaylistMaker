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
import com.example.playlistmaker.domain.models.TypeError
import com.example.playlistmaker.ui.search.SearchState
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

    private var trackIsAdded = false

    private var trackIds = mutableListOf<String>()

    private var timerJob: Job? = null

    private val playerState =
        MutableLiveData<PlayerState>(
            PlayerState.Default(
                getCurrentPosition(),
                trackIsLicked,
                trackIsAdded
            )
        )

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
            playerState.postValue(
                PlayerState.Prepared(
                    getCurrentPosition(),
                    trackIsLicked,
                    trackIsAdded
                )
            )
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

    fun addButtonClicked() {
        viewModelScope.launch {
            newPlaylistInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist): Boolean {
        if (true) {
            viewModelScope.launch {
                newPlaylistInteractor.addTrackToPlaylist(track, playlist.also {
                    trackIds.add(track.trackId)
                    it.size += 1
                })
            }
            return true
        } else {
            return false
        }



    }

    private fun processResult(playlists: List<Playlist>) {
        playerState.postValue(
            PlayerState.ButtomSheet(
                isPlayButtonEnabled = playerState.value!!.isPlayButtonEnabled,
                isButtonPlay = playerState.value!!.isPlayButtonEnabled,
                progress = playerState.value!!.progress,
                isLiked = playerState.value!!.isLiked,
                isAdded = playerState.value!!.isAdded,
                playlists = playlists
            )
        )

//        trackIds = playlists.map {
//
//        }

    }

    fun startPlayer() {
        playerInteractor.start()
        playerState.postValue(
            PlayerState.Playing(
                getCurrentPosition(),
                trackIsLicked,
                trackIsAdded
            )
        )
        startTimer()
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(DELAY_MILLIS)
                playerState.postValue(
                    PlayerState.Playing(
                        getCurrentPosition(),
                        trackIsLicked,
                        trackIsAdded
                    )
                )
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPosition(), trackIsLicked, trackIsAdded))
    }

    private fun getCurrentPosition(): String {
        return dateFormat.format(playerInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        playerState.postValue(
            PlayerState.Default(
                getCurrentPosition(),
                trackIsLicked,
                trackIsAdded
            )
        )
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }

}