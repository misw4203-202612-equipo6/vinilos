package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.model.Performer

@Composable
fun MusicianListElement(performer: Performer){
    Row{
        AsyncImage(
            model = performer.image,
            contentDescription = performer.description,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.mi_imagen),
            error = painterResource(id = R.drawable.mi_imagen)
        )
        Column{
            Text(performer.name)
            Text(performer.description)
        }
    }
}