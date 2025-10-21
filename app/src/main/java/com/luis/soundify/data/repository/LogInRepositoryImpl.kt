package com.luis.soundify.data.repository

import com.luis.soundify.data.local.SecureStorage
import com.luis.soundify.data.mappers.map
import com.luis.soundify.data.remote.SpotifyAuthApiService
import com.luis.soundify.domain.models.TokenModel
import com.luis.soundify.domain.repository.LogInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogInRepositoryImpl @Inject constructor(
    private val spotifyAuthApiService: SpotifyAuthApiService,
    private val secureStorage: SecureStorage
) : LogInRepository {

    override fun getAccessToken(code: String, codeVerifier: String): Flow<Result<TokenModel>> {
        return flow {
            val response = spotifyAuthApiService.getAccessToken(
                code = code,
                codeVerifier = codeVerifier
            )

            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    val tokenModel = tokenResponse.map()
                    secureStorage.saveToken(tokenModel.accessToken)
                    secureStorage.saveRefreshToken(tokenModel.refreshToken ?: "")

                    emit(
                        Result.success(tokenModel)
                    )
                } ?: run {
                    emit(Result.failure(Exception("Auth Error, no body structure found!!")))
                }
            } else {
                emit(Result.failure(Exception("Auth Error, Access Token service no work")))
            }
        }
    }

    override suspend fun refreshAccessTokenSynchronous(): Result<Unit> {
        val refreshToken = secureStorage.getRefreshToken()
            ?: return Result.failure(Exception("No refresh token stored"))
        val response = spotifyAuthApiService.refreshToken(refreshToken = refreshToken)

        return if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                secureStorage.saveToken(tokenResponse.accessToken)
                secureStorage.saveRefreshToken(tokenResponse.refreshToken ?: "")

                Result.success(Unit)
            } ?: Result.failure(Exception("Request refresh token Error, no body structure found!!"))
        } else {
            Result.failure(Exception("Request refresh token failed with code: ${response.code()}"))
        }
    }
}