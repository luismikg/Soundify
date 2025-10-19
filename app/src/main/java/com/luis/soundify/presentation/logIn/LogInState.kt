package com.luis.soundify.presentation.logIn

import com.luis.soundify.domain.models.TokenModel

sealed class LogInState {
    data object START : LogInState()
    data object Loading : LogInState()
    data class Success(val tokenModel: TokenModel) : LogInState()
    data class Error(val error: String) : LogInState()
}