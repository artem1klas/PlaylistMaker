package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        var ids = ""
        if (playlist.trackIds.isNotEmpty()){
            ids = playlist.trackIds.joinToString(",")
        }
        return PlaylistEntity(
            id = playlist.id,
            namePlaylist = playlist.namePlaylist,
            descriptionPlaylist = playlist.descriptionPlaylist,
            uri = playlist.uri,
            ids,
            size = playlist.size
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        val ids = mutableListOf<String>()
        if (playlist.trackIds.isNotEmpty()){
            ids.addAll(playlist.trackIds.split(",").toMutableList())
        }
        return Playlist(
            playlist.id,
            playlist.namePlaylist,
            playlist.descriptionPlaylist,
            playlist.uri,
            ids,
            playlist.size
        )
    }

}