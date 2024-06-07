package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.models.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            namePlaylist = playlist.namePlaylist,
            descriptionPlaylist = playlist.descriptionPlaylist,
            uri = playlist.uri,
            //       val tracks: List<String>,
            size = playlist.size
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.namePlaylist,
            playlist.descriptionPlaylist,
            playlist.uri,
            //       val tracks: List<String>,
            playlist.size
        )
    }

}