package com.luis.soundify.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.soundify.R
import com.luis.soundify.domain.models.SongModel

@Composable
fun SongItem(
    songModel: SongModel,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box() {
            Image(
                painter = painterResource(id = R.drawable.soundifylogo),
                contentDescription = stringResource(R.string.shadow),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .alpha(0.5f)
                    .size(74.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .align(Alignment.BottomCenter)
            )

            Image(
                painter = painterResource(id = R.drawable.play_sound),
                contentDescription = stringResource(R.string.singer),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = songModel.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = songModel.artist,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
private fun AlbumItemPreview() {

    SongItem(
        songModel = SongModel(
            "Name 1",
            "Artista 1",
            urlSpotify = ""
        )
    )

}