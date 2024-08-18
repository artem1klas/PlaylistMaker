package com.example.playlistmaker.data.db.database_dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.TrackInPlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(trackInPlaylistEntity: TrackInPlaylistEntity)

    @Query("DELETE FROM track_in_playlist_table WHERE trackId = :id")
    suspend fun deteteTrackFromPlaylist(id: String)

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylist(id: Int): PlaylistEntity

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId IN (:idsTracks)")
    suspend fun getTracksInPlaylist(idsTracks: List<Int>): List<TrackInPlaylistEntity>

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

}