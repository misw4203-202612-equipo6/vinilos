package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vynils.R
import com.example.vynils.model.Track

@Composable
fun TrackListElement(track: Track){
    Row{
        Image(
            painter = painterResource(id = R.drawable.no_image),
            contentDescription = "Descripción",
            modifier = Modifier.size(80.dp)
        )
        Column{
            Text(track.name)
            Text(track.duration)
        }
    }
}