package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey


    @Entity(tableName = "playlist_table")
    data class PlaylistEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val namePlaylist: String,
        val descriptionPlaylist: String,
        val uri: String,
 //       val tracks: List<String>,
        val size: Int
    )
