package com.example.playlistmaker.media.ui.favoritetracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavoriteTrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TypeError
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>(FavoriteState.Empty)
    fun observeState(): LiveData<FavoriteState> = stateLiveData
   fun fillData() {
       renderState(FavoriteState.Loading)
       viewModelScope.launch {
           favoriteTrackInteractor
               .getTracks()
               .collect{
                   tracks -> processResult(tracks)
               }
       }
   }
    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()){
            renderState(FavoriteState.Empty)
        } else {
            renderState(FavoriteState.Content(tracks))
        }
    }
    private fun renderState(state: FavoriteState) {
        stateLiveData.postValue(state)
    }

}