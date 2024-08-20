package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int,
    var namePlaylist: String,
    var descriptionPlaylist: String,
    var uri: String,
    var trackIds: MutableList<String>,
    var size: Int
)