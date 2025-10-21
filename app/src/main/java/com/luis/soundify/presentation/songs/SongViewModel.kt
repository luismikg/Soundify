package com.luis.soundify.presentation.songs

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
class SongViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _state: MutableStateFlow<SongState> = MutableStateFlow(SongState.Starting)
    val state: StateFlow<SongState> = _state

    fun getSongs(albumId: String) {
        _state.value = SongState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            musicRepository.getSongsByAlbum(albumId = albumId).collect { result ->
                result.onSuccess { songList ->
                    _state.value = SongState.Success(songList = songList)
                }
                result.onFailure {
                    _state.value = SongState.Error("No album found!")
                }
            }
        }

    }
}