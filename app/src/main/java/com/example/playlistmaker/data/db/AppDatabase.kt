package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.data.converters.TrackIdsConverter
import com.example.playlistmaker.data.db.playlists.PlaylistDao
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.data.db.tracks.TrackDao
import com.example.playlistmaker.data.db.tracks.TrackEntity
import com.example.playlistmaker.data.db.tracks.TrackInPlaylistEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class])
//@TypeConverters(TrackIdsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao

}