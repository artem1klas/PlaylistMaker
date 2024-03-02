package com.example.playlistmaker.player.ui

sealed interface PlayerState {
    object Default: PlayerState
    object Prepared: PlayerState
    data class Playing(var time: String): PlayerState
    data class Paused(var time: String): PlayerState
}