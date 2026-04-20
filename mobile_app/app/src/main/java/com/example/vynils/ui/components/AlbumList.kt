package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vynils.model.Album

@Composable
fun AlbumList(
    albums: List<Album>,
    onAlbumClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLazy: Boolean = true
) {
    if (isLazy) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(albums) { album ->
                AlbumListElement(album, onClick = { onAlbumClick(album.id) })
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        }
    } else {
        Column(modifier = modifier) {
            albums.forEach { album ->
                AlbumListElement(album, onClick = { onAlbumClick(album.id) })
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        }
    }
}