package com.luis.soundify.di

import android.content.Context
import com.luis.soundify.data.DataConstants
import com.luis.soundify.data.local.SecureStorage
import com.luis.soundify.data.remote.SpotifyApiClient
import com.luis.soundify.data.remote.SpotifyAuthApiService
import com.luis.soundify.data.remote.SpotifyAuthInterceptor
import com.luis.soundify.data.remote.SpotifyAuthenticator
import com.luis.soundify.data.repository.LogInRepositoryImpl
import com.luis.soundify.data.repository.MusicRepositoryImpl
import com.luis.soundify.domain.repository.LogInRepository
import com.luis.soundify.domain.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun provideSpotifyApiClient(
        secureStorage: SecureStorage,
        logInRepository: LogInRepository
    ): SpotifyApiClient {

        val client = OkHttpClient.Builder()
            .authenticator(SpotifyAuthenticator(secureStorage, logInRepository))
            .addInterceptor(SpotifyAuthInterceptor(secureStorage))
            .build()

        return Retrofit
            .Builder()
            .baseUrl(DataConstants.BASE_URL_CLIENT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApiClient::class.java)
    }

    @Provides
    fun provideSecureStorage(@ApplicationContext context: Context): SecureStorage {
        return SecureStorage(context = context)
    }

    @Provides
    fun provideLoginRepository(
        spotifyAuthApiService: SpotifyAuthApiService,
        secureStorage: SecureStorage
    ): LogInRepository {
        return LogInRepositoryImpl(
            spotifyAuthApiService = spotifyAuthApiService,
            secureStorage = secureStorage
        )
    }

    @Provides
    fun provideMusicRepository(spotifyApiClient: SpotifyApiClient): MusicRepository {
        return MusicRepositoryImpl(spotifyApiClient = spotifyApiClient)
    }
}