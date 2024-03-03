package com.example.playlistmaker.player.ui

sealed interface PlayerState {
    object Default: PlayerState
    object Prepared: PlayerState
    data class Playing(val time: String): PlayerState
    data class Paused(val time: String): PlayerState
}