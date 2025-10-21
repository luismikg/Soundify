package com.luis.soundify.presentation.album

import com.luis.soundify.domain.models.AlbumModel

sealed class AlbumState {
    data object Starting : AlbumState()
    data object Loading : AlbumState()
    data class Success(val albumList: List<AlbumModel>) : AlbumState()
    data class Error(val error: String) : AlbumState()
}