package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {
    val gson = Gson()
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            namePlaylist = playlist.namePlaylist,
            descriptionPlaylist = playlist.descriptionPlaylist,
            uri = playlist.uri,
            trackIds = playlist.trackIds.joinToString(","),
            size = playlist.size
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {

        return Playlist(
            playlist.id,
            playlist.namePlaylist,
            playlist.descriptionPlaylist,
            playlist.uri,
            playlist.trackIds.split(",").toMutableList(),
            playlist.size
        )
    }

}