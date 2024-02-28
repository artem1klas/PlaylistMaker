package com.example.playlistmaker.player.ui

sealed interface PlayerState {
    object Prepared: PlayerState
    data class Playing(var time: String): PlayerState
    object Paused: PlayerState
}