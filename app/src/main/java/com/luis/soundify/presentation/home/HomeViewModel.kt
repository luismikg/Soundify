package com.luis.soundify.presentation.home

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
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    init {
        getGenreMusic()
    }

    private fun getGenreMusic() {
        _state.value = HomeState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            musicRepository.getGenresSample().collect { result ->
                result.onSuccess { listGenreModel ->
                    _state.value = HomeState.Success(listGenreModel.mapIndexed { index, model ->
                        GenreUIModel(
                            name = model.name,
                            urlImage = model.urlImage,
                            colorIndex = index % 10
                        )
                    })
                }
            }
        }
    }
}