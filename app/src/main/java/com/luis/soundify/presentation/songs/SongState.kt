package com.luis.soundify.presentation.songs

import com.luis.soundify.domain.models.SongModel

sealed class SongState {
    data object Starting : SongState()
    data object Loading : SongState()
    data class Success(val songList: List<SongModel>) : SongState()
    data class Error(val error: String) : SongState()
}