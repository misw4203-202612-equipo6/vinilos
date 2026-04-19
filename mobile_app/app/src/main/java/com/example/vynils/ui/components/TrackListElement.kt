package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vynils.R
import com.example.vynils.model.Track

@Composable
fun TrackListElement(track: Track) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .testTag("artist-track-item")
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_image),
            contentDescription = "Descripción",
            modifier = Modifier.size(80.dp)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(track.name)
            Text(track.duration)
        }
    }
}
