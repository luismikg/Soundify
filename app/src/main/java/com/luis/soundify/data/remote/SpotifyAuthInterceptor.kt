package com.luis.soundify.data.remote

import com.luis.soundify.data.local.SecureStorage
import okhttp3.Interceptor
import okhttp3.Response

class SpotifyAuthInterceptor(private val secureStorage: SecureStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = secureStorage.getToken()
        val request = chain.request().newBuilder()
        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(request.build())
    }
}