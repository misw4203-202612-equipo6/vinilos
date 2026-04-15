package com.example.vynils.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.vynils.model.Album

@Composable
fun AlbumList(albums: List<Album>){
    LazyColumn() {
        items(albums) { album ->
            AlbumListElement(album)
        }
    }
}