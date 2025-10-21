package com.luis.soundify.data.mappers

import com.luis.soundify.data.entities.albums.AlbumResponse
import com.luis.soundify.domain.models.AlbumModel

fun AlbumResponse.map(): List<AlbumModel> {
    return this.items.map { item ->
        AlbumModel(
            id = item.id,
            name = item.name,
            releaseDate = item.release_date,
            urlImage = item.images.firstOrNull()?.url ?: "",
        )
    }
}