package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.model.Musician

@Composable
fun MusicianListElement(musician: Musician) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 104.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val musicianImage = if (musician.image.startsWith("http://")) {
            musician.image.replace("http://", "https://")
        } else {
            musician.image
        }

        AsyncImage(
            model = if (musicianImage.isEmpty()) null else musicianImage,
            contentDescription = musician.name,
            modifier = Modifier
                .size(width = 78.dp, height = 78.dp)
                .clip(RoundedCornerShape(8.dp)),
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
                text = musician.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = musician.description ?: "",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}
