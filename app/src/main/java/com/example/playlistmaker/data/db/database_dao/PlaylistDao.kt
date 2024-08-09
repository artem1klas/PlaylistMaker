package com.example.playlistmaker.data.db.database_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.TrackInPlaylistEntity
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.TrackInPlaylist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(trackInPlaylistEntity: TrackInPlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylist(id: Int): PlaylistEntity

    @Query("SELECT * FROM playlist_table WHERE id IN (:idsTracks)")
    suspend fun getTracksInPlaylist(idsTracks: List<Int>): List<TrackInPlaylistEntity>
}