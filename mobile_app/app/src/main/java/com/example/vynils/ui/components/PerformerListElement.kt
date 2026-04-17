package com.example.vynils.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.model.Performer

@Composable
fun PerformerListElement(
    performer: Performer,
    onClick: (() -> Unit)? = null,
    tagPrefix: String = "performer",
    showDetailButton: Boolean = false,
    showDescription: Boolean = false
) {
    val performerImage = if (performer.image.startsWith("http://")) {
        performer.image.replace("http://", "https://")
    } else {
        performer.image
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .heightIn(min = 104.dp)
            .testTag("${tagPrefix}-item")
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (performerImage.isEmpty()) null else performerImage,
            contentDescription = performer.name,
            modifier = Modifier
                .size(width = 78.dp, height = 78.dp)
                .testTag("${tagPrefix}-photo")
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
                text = performer.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.testTag("${tagPrefix}-name")
            )
            if (showDescription) {
                Text(
                    text = performer.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (showDetailButton && onClick != null) {
                TextButton(
                    onClick = onClick,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .testTag("${tagPrefix}-detail-button")
                ) {
                    Text(text = "Ver detalle")
                }
            }
        }
    }
}
