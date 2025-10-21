package com.luis.soundify.data.repository

import com.luis.soundify.data.mappers.map
import com.luis.soundify.data.remote.SpotifyApiClient
import com.luis.soundify.domain.models.AlbumModel
import com.luis.soundify.domain.models.ArtistSearchModel
import com.luis.soundify.domain.models.GenreModel
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

    override fun getAlbumsByArtist(artistId: String): Flow<Result<List<AlbumModel>>> {
        return flow {
            val response = spotifyApiClient.getArtistAlbums(artistId = artistId)
            if (response.isSuccessful) {
                response.body()?.let { albumResponse ->
                    val albumModel = albumResponse.map()
                    emit(Result.success(albumModel))
                }
            } else {
                emit(Result.failure(Exception("Error fetching albums for artist: $artistId")))
            }
        }
    }

    override fun getGenresSample(): Flow<Result<List<GenreModel>>> {

        return flow {

            val sampleGenres = listOf(
                GenreModel(
                    "Rock",
                    "https://i.scdn.co/image/ab67616d0000b2731c95b58477e1cc3337869ed6",
                ),
                GenreModel(
                    "Pop",
                    "https://i.scdn.co/image/ab6761610000e5eb597f9edd2cd1a892d4412b09",
                ),
                GenreModel(
                    "Hip-Hop",
                    "https://i.scdn.co/image/ab6761610000e5ebc19a88576ebe6fcbb325c297",
                ),
                GenreModel(
                    "Jazz",
                    "https://i.scdn.co/image/fc4e0f474fb4c4cb83617aa884dc9fd9822d4411",
                ),
                GenreModel(
                    "Blues",
                    "https://i.scdn.co/image/ab6761610000e5eb09882b1b7b33732abd60fc38",
                ),
                GenreModel(
                    "Classical",
                    "https://i.scdn.co/image/ab6761610000e5eba636b0b244253f602a629796",
                ),
                GenreModel(
                    "Reggae",
                    "https://i.scdn.co/image/ab6761610000e5eb8f95c6e5315e196a970c38cd",
                ),
                GenreModel(
                    "Dance",
                    "https://i.scdn.co/image/ab6761610000e5eb82fc5e0ee0a525bfc037d4d7",
                ),
                GenreModel(
                    "Electronic",
                    "https://i.scdn.co/image/ab6761610000e5eb3b2498dc8a8c6eceed0a2db3",
                ),
                GenreModel(
                    "Soul",
                    "https://i.scdn.co/image/ab6761610000e5eb8a20d3aa1b91cc7bd14bd6b9",
                )
            )

            emit(Result.success(sampleGenres))
        }
    }
}