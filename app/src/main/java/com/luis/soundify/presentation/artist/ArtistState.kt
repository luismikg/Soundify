package com.luis.soundify.presentation.artist

import com.luis.soundify.domain.models.ArtistSearchModel

sealed class ArtistState {
    data object Loading : ArtistState()
    data class Search(val genre: String) : ArtistState()
    data class Success(val genre: String, val artistList: List<ArtistSearchModel>) : ArtistState()
    data class Error(val error: String) : ArtistState()
}