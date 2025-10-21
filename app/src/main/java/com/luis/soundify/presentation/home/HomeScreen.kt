package com.luis.soundify.presentation.home

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.luis.soundify.R
import com.luis.soundify.presentation.composables.Loading
import com.luis.soundify.presentation.theme.LocalAppColors

@Composable
fun HomeScreen(onNavigateToArtist: (String) -> Unit) {

    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    when (state) {
        HomeState.Loading -> {
            Loading()
        }

        is HomeState.Success -> {
            val data = (state as HomeState.Success).listGenreModel
            HomeScreenContainer(data = data, onNavigateToArtist = onNavigateToArtist)
        }
    }
}

@Composable
private fun HomeScreenContainer(
    data: List<GenreUIModel>,
    onNavigateToArtist: (String) -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 12.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.soundifylogo),
                    contentDescription = stringResource(R.string.login_img_girl_desc),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = stringResource(R.string.search),
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.blueSky
                    ),
                )
            }

            Button(
                onClick = { onNavigateToArtist(data.random().name) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.random_genre),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.size(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = stringResource(R.string.top_genre),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                items(data.take(4)) { genre ->
                    GenreCardItem(
                        genre = genre,
                        backgroundColor = genreColors[genre.colorIndex],
                        onClick = { onNavigateToArtist(genre.name) }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = stringResource(R.string.explore),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                    )
                }

                items(data.drop(4)) { genre ->
                    GenreCardItem(
                        genre = genre,
                        backgroundColor = genreColors[genre.colorIndex],
                        onClick = { onNavigateToArtist(genre.name) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.size(80.dp))
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

@Composable
fun GenreCardItem(
    genre: GenreUIModel,
    backgroundColor: Color,
    cardHeight: Dp = 110.dp,
    imageRotation: Float = 20f,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(cardHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(
            text = genre.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .widthIn(max = 100.dp)
                .padding(8.dp)
        )

        AsyncImage(
            model = genre.urlImage,
            contentDescription = genre.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp)
                .offset(x = 10.dp, y = 10.dp)
                .graphicsLayer { rotationZ = imageRotation }
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

val genreColors = listOf(
    Color(0xFF8BC34A),
    Color(0xFF9C27B0),
    Color(0xFF3F51B5),
    Color(0xFFFF9800),
    Color(0xFF03A9F4),
    Color(0xFF673AB7),
    Color(0xFF303F9F),
    Color(0xFFE64A19),
    Color(0xFF795548),
    Color(0xFF4CAF50),
)

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val sampleGenres = listOf(
        GenreUIModel(
            "Rock",
            "https://i.scdn.co/image/ab6761610000e5eb6fc460f10177fa38af69b8bf",
            0
        ),
        GenreUIModel(
            "Pop",
            "https://i.scdn.co/image/ffb3ff26238fe635a230bb0feb59dd0a5b209b6f",
            1
        ),
        GenreUIModel(
            "Hip-Hop",
            "https://i.scdn.co/image/ab6761610000e5eb09882b1b7b33732abd60fc38",
            2
        ),
        GenreUIModel(
            "Jazz",
            "https://i.scdn.co/image/ab6761610000e5eb2bc0400a72af84244f30bd6a",
            3
        ),
        GenreUIModel(
            "Blues",
            "https://i.scdn.co/image/ab6761610000e5ebc8752dd511cda8c31e9daee8",
            4
        ),
        GenreUIModel(
            "Classical",
            "https://i.scdn.co/image/ab676161000051746fc460f10177fa38af69b8bf",
            5
        ),
        GenreUIModel(
            "Reggae",
            "https://i.scdn.co/image/8b4bbc1e769cc1cf78a6a369f3a8b0026631eb96",
            6
        ),
        GenreUIModel(
            "Dance",
            "https://i.scdn.co/image/ab6761610000f17809882b1b7b33732abd60fc38",
            7
        ),
        GenreUIModel(
            "Electronic",
            "https://i.scdn.co/image/ab6761610000f1782bc0400a72af84244f30bd6a",
            8
        ),
        GenreUIModel(
            "Soul",
            "https://i.scdn.co/image/ab6761610000f178c8752dd511cda8c31e9daee8",
            9
        )
    )

    MaterialTheme {
        HomeScreenContainer(data = sampleGenres, onNavigateToArtist = {})
    }
}

