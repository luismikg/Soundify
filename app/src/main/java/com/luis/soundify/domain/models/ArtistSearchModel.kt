package com.luis.soundify.domain.models

data class ArtistSearchModel(
    val id: String,
    val name: String,
    val urlImage: String,
    val followersTotal: Int,
    val genres: String,
    val popularity: Int,
    val externalProfileUrl: String
)