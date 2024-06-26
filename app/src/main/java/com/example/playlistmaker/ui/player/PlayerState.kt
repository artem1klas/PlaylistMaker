package com.example.playlistmaker.ui.player

import com.example.playlistmaker.domain.models.Playlist

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val isButtonPlay: Boolean,
    val progress: String,
    var isLiked: Boolean,
    var isAdded: Boolean,
    var playlists: List<Playlist>
) {
    class Default(progress: String, isLicked: Boolean, isAdded: Boolean) :
        PlayerState(false, true, progress, isLicked, isAdded, mutableListOf())

    class Prepared(progress: String, isLicked: Boolean, isAdded: Boolean) :
        PlayerState(true, true, progress, isLicked, isAdded, mutableListOf())

    class Playing(progress: String, isLicked: Boolean, isAdded: Boolean) :
        PlayerState(true, false, progress, isLicked, isAdded, mutableListOf())

    class Paused(progress: String, isLicked: Boolean, isAdded: Boolean) :
        PlayerState(true, true, progress, isLicked, isAdded, mutableListOf())

    class ButtomSheet(
        isPlayButtonEnabled: Boolean,
        progress: String,
        isButtonPlay: Boolean,
        isLiked: Boolean,
        isAdded: Boolean,
        playlists: List<Playlist>
    ) :
        PlayerState(isPlayButtonEnabled, isButtonPlay, progress, isLiked, isAdded, playlists)
}