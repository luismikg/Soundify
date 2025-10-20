package com.luis.soundify.domain.repository

import com.luis.soundify.domain.models.ArtistSearchModel
import com.luis.soundify.domain.models.GenreModel
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun getArtistSearch(query: String): Flow<Result<List<ArtistSearchModel>>>
    fun getGenresSample(): Flow<Result<List<GenreModel>>>
}