package com.luis.soundify.presentation.artist

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
import com.luis.soundify.R
import com.luis.soundify.domain.models.ArtistSearchModel
import com.luis.soundify.presentation.composables.ArtistItem
import com.luis.soundify.presentation.composables.Loading
import com.luis.soundify.presentation.theme.LocalAppColors
import com.luis.soundify.presentation.theme.SoundifyTheme

@Composable
fun ArtistScreen(
    artistViewModel: ArtistViewModel,
    onBackClick: () -> Unit,
    onArtistClick: (ArtistSearchModel) -> Unit
) {

    val state by artistViewModel.state.collectAsState()

    when (state) {
        is ArtistState.Search -> {
            val genre = (state as ArtistState.Search).genre
            artistViewModel.getArtistSearch(genre = genre)
            Loading()
        }

        is ArtistState.Loading -> {
            ArtistScreenContainer(
                genreTitle = stringResource(R.string.searching),
                data = emptyList(),
                onBackClick = onBackClick,
                onArtistClick = onArtistClick
            )
            Loading()
        }

        is ArtistState.Success -> {
            val resultSuccess = (state as ArtistState.Success)
            ArtistScreenContainer(
                genreTitle = resultSuccess.genre,
                data = resultSuccess.artistList,
                onBackClick = onBackClick,
                onArtistClick = onArtistClick
            )
        }

        is ArtistState.Error -> {
            val message = (state as ArtistState.Error).error
            onBackClick()
        }
    }
}

@Composable
fun ArtistScreenContainer(
    genreTitle: String,
    data: List<ArtistSearchModel>,
    onBackClick: () -> Unit,
    onArtistClick: (ArtistSearchModel) -> Unit
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
                    text = genreTitle,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.top_singers),
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(R.string.explore_singers),
                    fontSize = 14.sp,
                    color = LocalAppColors.current.blueSky,
                    modifier = Modifier.clickable {
                        val artist = data.random()
                        artist.genre = genreTitle
                        onArtistClick(artist)
                    }
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { artist ->
                    artist.genre = genreTitle
                    ArtistItem(
                        artist = artist,
                        onClick = { onArtistClick(artist) }
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
        ArtistSearchModel(
            "ID",
            "Conan Gray",
            "https://placehold.co/120x120/1DB954/FFFFFF/png?text=CG",
            20_000_000,
            "Pop, Indie",
            90,
            ""
        ),
        ArtistSearchModel(
            "ID",
            "Chase Atlantic",
            "https://placehold.co/120x120/1DB954/FFFFFF/png?text=CA",
            15_000_000,
            "R&B, Alternative",
            85,
            ""
        ),
        ArtistSearchModel(
            "ID",
            "beabadoobee",
            "https://placehold.co/120x120/1DB954/FFFFFF/png?text=BB",
            10_000_000,
            "Indie Rock",
            80,
            ""
        ),
        ArtistSearchModel(
            "ID",
            "NewJeans",
            "https://placehold.co/120x120/1DB954/FFFFFF/png?text=NJ",
            8_000_000,
            "Kpop",
            95,
            ""
        ),
        ArtistSearchModel(
            "ID",
            "keshi",
            "https://placehold.co/120x120/1DB954/FFFFFF/png?text=KS",
            12_000_000,
            "R&B, Lo-Fi",
            88,
            ""
        ),
    )

    SoundifyTheme {
        ArtistScreenContainer(
            genreTitle = "Your Library",
            data = previewArtistsData,
            onBackClick = {},
            onArtistClick = {}
        )
    }
}