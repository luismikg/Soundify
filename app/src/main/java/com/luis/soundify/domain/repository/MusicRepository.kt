package com.luis.soundify.domain.repository

import com.luis.soundify.domain.models.ArtistSearchModel
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun getArtistSearch(query: String): Flow<Result<List<ArtistSearchModel>>>
}