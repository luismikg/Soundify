package com.luis.soundify.di

import com.luis.soundify.data.DataConstants
import com.luis.soundify.data.SpotifyApiClient
import com.luis.soundify.data.SpotifyAuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideSpotifyAuthApiService(): SpotifyAuthApiService {
        return Retrofit
            .Builder()
            .baseUrl(DataConstants.BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyAuthApiService::class.java)
    }

    @Provides
    fun provideSpotifyApiClient(): SpotifyApiClient {
        return Retrofit
            .Builder()
            .baseUrl(DataConstants.BASE_URL_CLIENT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApiClient::class.java)
    }
}