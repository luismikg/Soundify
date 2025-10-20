package com.luis.soundify.presentation.home

sealed class HomeState {
    data object Loading : HomeState()
    data class Success(val listGenreModel: List<GenreUIModel>) : HomeState()
}