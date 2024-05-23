package com.example.playlistmaker.ui.media.favoritetracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_impl.media.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>(FavoriteState.Empty)
    fun observeState(): LiveData<FavoriteState> = stateLiveData
    fun fillData() {
        renderState(FavoriteState.Loading)
        viewModelScope.launch {
            favoriteTrackInteractor
                .getTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty)
        } else {
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        stateLiveData.postValue(state)
    }

}