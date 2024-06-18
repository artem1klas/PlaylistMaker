package com.example.playlistmaker.data.db.tracks

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.playlists.PlaylistDao
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.models.Playlist

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}