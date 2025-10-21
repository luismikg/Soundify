package com.luis.soundify.presentation.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.soundify.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _state: MutableStateFlow<AlbumState> = MutableStateFlow(AlbumState.Starting)
    val state: StateFlow<AlbumState> = _state

    fun getAlbum(artistId: String) {
        _state.value = AlbumState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            musicRepository.getAlbumsByArtist(artistId = artistId).collect { result ->
                result.onSuccess { albumList ->
                    _state.value = AlbumState.Success(albumList = albumList)
                }
                result.onFailure {
                    _state.value = AlbumState.Error("No album found!")
                }
            }
        }

    }
}