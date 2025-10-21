package com.luis.soundify.data.mappers

import com.luis.soundify.data.entities.songs.SongResponse
import com.luis.soundify.domain.models.SongModel

fun SongResponse.map(): List<SongModel> {
    return this.items.map { item ->
        SongModel(
            name = item.name,
            artist = item.artists.joinToString { artist ->
                "${artist.name} • "
            },
            urlSpotify = item.external_urls.spotify
        )
    }
}