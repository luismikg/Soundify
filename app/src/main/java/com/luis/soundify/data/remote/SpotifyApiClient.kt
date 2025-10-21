package com.luis.soundify.data.remote

import com.luis.soundify.data.entities.albums.AlbumResponse
import com.luis.soundify.data.entities.searchArtists.ArtistSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApiClient {

    @GET("search")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<ArtistSearchResponse>

    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") artistId: String,
        @Query("include_groups") includeGroups: String = "album,single,appears_on,compilation",
        @Query("limit") limit: Int = 50
    ): Response<AlbumResponse>
}