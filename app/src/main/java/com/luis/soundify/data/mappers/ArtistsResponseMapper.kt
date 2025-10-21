package com.luis.soundify.data.mappers

import com.luis.soundify.data.entities.searchArtists.Artists
import com.luis.soundify.domain.models.ArtistSearchModel

fun Artists.map(): List<ArtistSearchModel> {
    return this.items.map { artist ->
        ArtistSearchModel(
            id = artist.id,
            name = artist.name,
            urlImage = artist.images.firstOrNull()?.url ?: "",
            followersTotal = artist.followers.total,
            genres = artist.genres.joinToString(", "),
            popularity = artist.popularity,
            externalProfileUrl = artist.external_urls.spotify
        )
    }
}