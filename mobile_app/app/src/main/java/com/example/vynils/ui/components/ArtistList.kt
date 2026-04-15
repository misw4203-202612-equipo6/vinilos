package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vynils.model.Artist

@Composable
fun ArtistList(
    artists: List<Artist>,
    onArtistClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLazy: Boolean = true
) {
    if (isLazy) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(artists) { artist ->
                ArtistListElement(artist, onClick = { onArtistClick(artist.id) })
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        }
    } else {
        Column(modifier = modifier) {
            artists.forEach { artist ->
                ArtistListElement(artist, onClick = { onArtistClick(artist.id) })
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        }
    }
}