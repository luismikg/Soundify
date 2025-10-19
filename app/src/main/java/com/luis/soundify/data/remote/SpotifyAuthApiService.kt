package com.luis.soundify.data.remote

import com.luis.soundify.BuildConfig
import com.luis.soundify.data.entities.TokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyAuthApiService {
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = BuildConfig.REDIRECT_URI,
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST("api/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String
    ): Response<TokenResponse>
}