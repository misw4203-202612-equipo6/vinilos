package com.example.vynils.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynils.ui.viewmodel.AlbumViewModel
import com.example.vynils.ui.components.SearchBar

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import com.example.vynils.ui.components.AlbumList

@Composable
fun AlbumListScreen(
    viewModel: AlbumViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAlbums()
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
        AlbumList(state.albums)
    }
}