package com.luis.soundify.domain.repository

import com.luis.soundify.domain.models.TokenModel
import kotlinx.coroutines.flow.Flow

interface LogInRepository {

    fun getAccessToken(code: String, codeVerifier: String): Flow<Result<TokenModel>>
}