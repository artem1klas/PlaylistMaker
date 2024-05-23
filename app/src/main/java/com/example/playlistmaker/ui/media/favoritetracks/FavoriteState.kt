package com.example.playlistmaker.ui.media.favoritetracks

import com.example.playlistmaker.domain.models.Track

interface FavoriteState {
    object Loading : FavoriteState

    data class Content(val tracks: List<Track>) : FavoriteState

    object Empty : FavoriteState
}