package com.example.playlistmaker.player.ui

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val isButtonPlay: Boolean,
    val progress: String,
    var isLiked: Boolean
) {
    class Default(progress: String, isLicked: Boolean) : PlayerState(false, true, progress, isLicked)
    class Prepared(progress: String, isLicked: Boolean) : PlayerState(true, true, progress, isLicked)
    class Playing(progress: String, isLicked: Boolean) : PlayerState(true, false, progress, isLicked)
    class Paused(progress: String, isLicked: Boolean) : PlayerState(true, true, progress, isLicked)
}