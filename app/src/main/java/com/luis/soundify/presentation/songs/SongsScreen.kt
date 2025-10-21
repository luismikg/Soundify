package com.luis.soundify.presentation.songs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.luis.soundify.R
import com.luis.soundify.core.toReadableDate
import com.luis.soundify.domain.models.AlbumModel
import com.luis.soundify.domain.models.SongModel
import com.luis.soundify.presentation.composables.Loading
import com.luis.soundify.presentation.composables.SongItem
import com.luis.soundify.presentation.theme.SoundifyTheme

@Composable
fun SongScreen(
    albumModel: AlbumModel,
    onBackClick: () -> Unit
) {

    val songViewModel: SongViewModel = hiltViewModel()
    val state by songViewModel.state.collectAsState()

    when (state) {

        is SongState.Starting -> {
            SongScreenContainer(
                albumModel = albumModel,
                data = emptyList(),
                onBackClick = onBackClick
            )
            Loading()

            songViewModel.getSongs(albumModel.id)
        }

        is SongState.Loading -> {
            SongScreenContainer(
                albumModel = albumModel,
                data = emptyList(),
                onBackClick = onBackClick
            )
            Loading()
        }

        is SongState.Success -> {
            val resultSuccess = (state as SongState.Success)
            SongScreenContainer(
                albumModel = albumModel,
                data = resultSuccess.songList,
                onBackClick = onBackClick
            )
        }

        is SongState.Error -> {
            val message = (state as SongState.Error).error
            onBackClick()
        }
    }
}

@Composable
fun SongScreenContainer(
    albumModel: AlbumModel,
    data: List<SongModel>,
    onBackClick: () -> Unit
) {

    val urlHandler = LocalUriHandler.current

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 28.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
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
                    text = "Genero: ${albumModel.genre}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.width(1.dp))
            }

            AsyncImage(
                model = albumModel.urlImage,
                contentDescription = albumModel.name,
                placeholder = painterResource(R.drawable.soundifylogo),
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(0.7f)
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .align(Alignment.CenterHorizontally)

            )

            Text(
                text = albumModel.name,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = albumModel.releaseDate.toReadableDate(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { song ->
                    SongItem(
                        songModel = song,
                        onClick = { urlHandler.openUri(song.urlSpotify) }
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
        SongModel(
            "Name 1",
            "Artista 1",
            urlSpotify = ""
        ),
        SongModel(
            "Name 1",
            "Artista 1",
            urlSpotify = ""
        ), SongModel(
            "Name 1",
            "Artista 1",
            urlSpotify = ""
        ),
        SongModel(
            "Name 1",
            "Artista 1",
            urlSpotify = ""
        )
    )

    SoundifyTheme {
        SongScreenContainer(
            albumModel = AlbumModel(
                "Rock", "id", "Name Album", "2025-03-25", ""
            ),
            data = previewArtistsData,
            onBackClick = {}
        )
    }
}