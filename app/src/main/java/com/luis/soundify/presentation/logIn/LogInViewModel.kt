package com.luis.soundify.presentation.logIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.soundify.domain.repository.LogInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val logInRepository: LogInRepository
) : ViewModel() {

    private val _state: MutableStateFlow<LogInState> = MutableStateFlow(LogInState.START)
    val state: StateFlow<LogInState> = _state

    fun startAuthorization() {
        _state.value = LogInState.Loading
    }

    fun authorizationFail() {
        _state.value = LogInState.START
    }

    fun requestAccessToken(code: String, codeVerifier: String) {
        _state.value = LogInState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            logInRepository
                .getAccessToken(code = code, codeVerifier = codeVerifier)
                .collect { result ->
                    result.onSuccess { tokenModel ->
                        _state.value = LogInState.Success(tokenModel)
                    }
                    result.onFailure { erro ->
                        _state.value =
                            LogInState.Error(erro.message ?: "Error at request access token")
                    }
                }
        }
    }

}