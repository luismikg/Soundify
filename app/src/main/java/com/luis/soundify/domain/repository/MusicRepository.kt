package com.luis.soundify.domain.repository

import com.luis.soundify.domain.models.AlbumModel
import com.luis.soundify.domain.models.ArtistSearchModel
import com.luis.soundify.domain.models.GenreModel
import com.luis.soundify.domain.models.SongModel
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun getArtistSearch(query: String): Flow<Result<List<ArtistSearchModel>>>
    fun getGenresSample(): Flow<Result<List<GenreModel>>>
    fun getAlbumsByArtist(artistId: String): Flow<Result<List<AlbumModel>>>
    fun getSongsByAlbum(albumId: String): Flow<Result<List<SongModel>>>
}