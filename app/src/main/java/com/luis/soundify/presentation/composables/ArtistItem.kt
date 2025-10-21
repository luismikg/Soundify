package com.luis.soundify.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luis.soundify.R
import com.luis.soundify.domain.models.ArtistSearchModel

@Composable
fun ArtistItem(
    artist: ArtistSearchModel,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = artist.urlImage,
            contentDescription = artist.name,
            placeholder = painterResource(R.drawable.soundifylogo),
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.5f))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = artist.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${artist.followersTotal / 1_000_000}${stringResource(R.string.followers)} • ${
                    artist.genres.split(",").firstOrNull() ?: stringResource(R.string.singer)
                }",
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
private fun ArtistItemPreview() {

    ArtistItem(
        artist = ArtistSearchModel(
            "ID",
            "keshi",
            "https://placehold.co/120x120/1DB954/FFFFFF/png?text=KS",
            12_000_000,
            "R&B, Lo-Fi",
            88,
            ""
        )
    )

}