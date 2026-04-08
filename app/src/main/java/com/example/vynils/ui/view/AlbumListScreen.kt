package com.example.vynils.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynils.ui.viewmodel.AlbumViewModel
import com.example.vynils.ui.components.AlbumListElement
import com.example.vynils.ui.components.SearchBar

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

@Composable
fun AlbumListScreen(
    viewModel: AlbumViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAlbumsMock()
    }
    Column{
        Row{
            Text("Albums")
            SearchBar()
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        }
        LazyColumn() {
            items(state.albums) { album ->
                AlbumListElement(album)
            }
        }
    }
}