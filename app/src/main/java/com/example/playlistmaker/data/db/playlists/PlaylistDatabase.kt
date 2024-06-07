package com.example.playlistmaker.data.db.playlists

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}