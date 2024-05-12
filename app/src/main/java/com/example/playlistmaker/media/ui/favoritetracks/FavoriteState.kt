package com.example.playlistmaker.media.ui.favoritetracks

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchState

interface FavoriteState {
    object Loading : FavoriteState

    data class Content(val tracks: List<Track>) : FavoriteState

    object Empty : FavoriteState
}