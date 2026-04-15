package com.example.vynils.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vynils.model.Artist

@Composable
fun ArtistList(musicians: List<Artist>, onArtistClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(musicians) { musician ->
            ArtistListElement(musician, onClick = { onArtistClick(musician.id) })
            HorizontalDivider(color = Color(0xFFD5D9E0))
        }
    }
}