package com.luis.soundify.domain.models

data class AlbumModel(
    var genre: String = "",
    val id: String,
    val name: String,
    val releaseDate: String,
    val urlImage: String,
)