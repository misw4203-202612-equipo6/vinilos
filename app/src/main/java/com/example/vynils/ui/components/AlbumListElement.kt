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
import com.example.vynils.model.Album

@Composable
fun AlbumListElement(album: Album){
    Row{
        Image(
            painter = painterResource(id = R.drawable.mi_imagen),
            contentDescription = "Descripción",
            modifier = Modifier.size(80.dp)
        )
        Column{
            Text(album.name)
            Text("Artista")
            Text(album.releaseDate)
        }
    }
}