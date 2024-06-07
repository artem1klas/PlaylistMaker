package com.example.playlistmaker.domain.models

import androidx.room.PrimaryKey

data class Playlist(
    val id: Int,
    val namePlaylist: String,
    val descriptionPlaylist: String,
    val uri: String,
    //       val tracks: List<String>,
    val size: Int
)