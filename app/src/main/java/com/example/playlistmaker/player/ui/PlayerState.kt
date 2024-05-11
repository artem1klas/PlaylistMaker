package com.example.playlistmaker.player.ui

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val isButtonPlay: Boolean, val progress: String) {
    class Default(progress: String): PlayerState(false, true, progress)
    class Prepared(progress: String): PlayerState(true, true, progress)
    class Playing(progress: String): PlayerState(true, false, progress)
    class Paused(progress: String): PlayerState(true, true, progress)
}