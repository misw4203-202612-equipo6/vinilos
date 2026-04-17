package com.example.vynils.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.model.Album
import com.example.vynils.utils.DateUtils

@Composable
fun AlbumListElement(album: Album, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .heightIn(min = 104.dp)
            .testTag("album-item")
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = album.cover,
            contentDescription = album.name,
            modifier = Modifier
                .size(width = 78.dp, height = 78.dp)
                .testTag("album-cover")
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.no_image),
            error = painterResource(id = R.drawable.no_image)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                album.name ?: "",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("album-name")
            )
            
            val artistName = if (album.artists?.isNotEmpty() == true) {
                album.artists[0].name ?: stringResource(id = R.string.unknown_artist)
            } else {
                stringResource(id = R.string.unknown_artist)
            }
            Text(
                text = artistName,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.testTag("album-artist")
            )

            Text(album.genre ?: "", color = Color.Gray)
            Text(DateUtils.formatRetrofitDate(album.releaseDate) ?: "")
            TextButton(
                onClick = onClick,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .testTag("album-detail-button")
            ) {
                Text(text = "Ver detalle")
            }
        }
    }
}
