package com.example.playlistmaker.data.db.playlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)


    @Query("SELECT * FROM track_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

//    @Query("SELECT namePlaylist FROM playlist_table")
//    suspend fun getNamesPlaylist(): List<String>

}