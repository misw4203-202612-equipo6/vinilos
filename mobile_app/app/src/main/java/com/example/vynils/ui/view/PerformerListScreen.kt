package com.example.vynils.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynils.ui.components.MusicianList
import com.example.vynils.ui.viewmodel.PerformerListScreenViewModel

@Composable
fun PerformerListScreen(
    viewModel: PerformerListScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadPerformers()
    }
    MusicianList(state.performers)
}