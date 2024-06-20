package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int,
    val namePlaylist: String,
    val descriptionPlaylist: String,
    val uri: String,
    val trackIds: MutableList<String>,
    var size: Int
)