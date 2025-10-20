package com.luis.soundify.data.remote

import com.luis.soundify.data.entities.searchArtists.ArtistSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApiClient {

    @GET("search")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<ArtistSearchResponse>
}