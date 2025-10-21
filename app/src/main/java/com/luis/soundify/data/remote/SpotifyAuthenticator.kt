package com.luis.soundify.data.remote

import com.luis.soundify.data.local.SecureStorage
import com.luis.soundify.domain.repository.LogInRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyAuthenticator(
    private val secureStorage: SecureStorage,
    private val logInRepository: LogInRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        val newAccessToken = secureStorage.getToken()
        if (response.request.header("Authorization") != null &&
            response.request.header("Authorization") == "Bearer $newAccessToken"
        ) {
            return null
        }

        val refreshSuccessful = runBlocking {
            logInRepository.refreshAccessTokenSynchronous().isSuccess
        }

        if (refreshSuccessful) {
            val freshToken = secureStorage.getToken()

            return response.request.newBuilder()
                .header("Authorization", "Bearer $freshToken")
                .build()
        }
        return null
    }
}