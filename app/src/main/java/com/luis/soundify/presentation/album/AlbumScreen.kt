package com.luis.soundify.presentation.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.luis.soundify.R
import com.luis.soundify.domain.models.AlbumModel
import com.luis.soundify.domain.models.ArtistSearchModel
import com.luis.soundify.presentation.composables.AlbumItem
import com.luis.soundify.presentation.composables.ArtistItem
import com.luis.soundify.presentation.composables.Loading
import com.luis.soundify.presentation.theme.LocalAppColors
import com.luis.soundify.presentation.theme.SoundifyTheme

@Composable
fun AlbumScreen(
    artistSearchModel: ArtistSearchModel,
    onBackClick: () -> Unit,
    onAlbumClick: (AlbumModel) -> Unit
) {

    val albumViewModel: AlbumViewModel = hiltViewModel()
    val state by albumViewModel.state.collectAsState()

    when (state) {

        is AlbumState.Starting -> {
            AlbumScreenContainer(
                artistSearchModel = artistSearchModel,
                data = emptyList(),
                onBackClick = onBackClick,
                onAlbumClick = onAlbumClick
            )
            Loading()

            albumViewModel.getAlbum(artistSearchModel.id)
        }

        is AlbumState.Loading -> {
            AlbumScreenContainer(
                artistSearchModel = artistSearchModel,
                data = emptyList(),
                onBackClick = onBackClick,
                onAlbumClick = onAlbumClick
            )
            Loading()
        }

        is AlbumState.Success -> {
            val resultSuccess = (state as AlbumState.Success)
            AlbumScreenContainer(
                artistSearchModel = artistSearchModel,
                data = resultSuccess.albumList,
                onBackClick = onBackClick,
                onAlbumClick = onAlbumClick
            )
        }

        is AlbumState.Error -> {
            val message = (state as AlbumState.Error).error
            onBackClick()
        }
    }
}

@Composable
fun AlbumScreenContainer(
    artistSearchModel: ArtistSearchModel,
    data: List<AlbumModel>,
    onBackClick: () -> Unit,
    onAlbumClick: (AlbumModel) -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 28.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = stringResource(R.string.back),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = stringResource(R.string.albums),
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                )
            }

            ArtistItem(
                artist = artistSearchModel
            )

            Text(
                text = stringResource(R.string.albums_added),
                fontSize = 14.sp,
                color = LocalAppColors.current.blueSky,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 22.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { album ->
                    album.genre = artistSearchModel.genre
                    AlbumItem(
                        albumModel = album,
                        onClick = { onAlbumClick(album) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.size(30.dp))
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.bottom_shadow),
            contentDescription = stringResource(R.string.shadow),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArtistListScreen() {
    val previewArtistsData = listOf(
        AlbumModel(
            "Genero 1",
            id = "id",
            "Name 1",
            "December 13, 2025",
            urlImage = ""
        ),
        AlbumModel(
            "Genero 1",
            id = "id",
            "Name 2",
            "December 13, 2025",
            urlImage = ""
        ),
        AlbumModel(
            "Genero 1",
            id = "id",
            "Name 1",
            "December 13, 2025",
            urlImage = ""
        ),
        AlbumModel(
            "Genero 1",
            id = "id",
            "Name 2",
            "December 13, 2025",
            urlImage = ""
        ),
    )

    SoundifyTheme {
        AlbumScreenContainer(
            artistSearchModel = ArtistSearchModel("", "", "", 8, "", 8, ""),
            data = previewArtistsData,
            onBackClick = {},
            onAlbumClick = {}
        )
    }
}