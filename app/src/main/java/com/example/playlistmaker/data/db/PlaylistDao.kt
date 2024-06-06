package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylisEntity)

//    @Delete
//    suspend fun removeTrack(track: TrackEntity)

//    @Query("SELECT * FROM track_table")
//    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT namePlaylist FROM playlist_table")
    suspend fun getNamesPlaylist(): List<String>

}