package com.luis.soundify.presentation.artist

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
class ArtistViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _state: MutableStateFlow<ArtistState> = MutableStateFlow(ArtistState.Search(""))
    val state: StateFlow<ArtistState> = _state

    fun searchGenre(genre: String) {
        _state.value = ArtistState.Search(genre = genre)
    }

    fun getArtistSearch(genre: String) {
        _state.value = ArtistState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            musicRepository.getArtistSearch(query = genre).collect { result ->
                result.onSuccess { artistList ->
                    _state.value = ArtistState.Success(genre = genre, artistList = artistList)
                }
                result.onFailure {
                    _state.value = ArtistState.Error("No artist found!")
                }
            }
        }

    }
}