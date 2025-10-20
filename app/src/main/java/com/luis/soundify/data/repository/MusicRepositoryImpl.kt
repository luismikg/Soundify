package com.luis.soundify.data.repository

import com.luis.soundify.data.mappers.map
import com.luis.soundify.data.remote.SpotifyApiClient
import com.luis.soundify.domain.models.ArtistSearchModel
import com.luis.soundify.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val spotifyApiClient: SpotifyApiClient
) : MusicRepository {

    override fun getArtistSearch(query: String): Flow<Result<List<ArtistSearchModel>>> {
        return flow {
            val response = spotifyApiClient.searchArtists(query = query)

            if (response.isSuccessful) {
                response.body()?.let { artistSearchResponse ->
                    val artistSearchModel = artistSearchResponse.artists.map()

                    emit(Result.success(artistSearchModel))
                }
            } else {
                emit(Result.failure(Exception("Search Error, Searching: $query")))
            }
        }
    }
}