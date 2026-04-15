package com.example.vynils.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynils.ui.components.CollectorList
import com.example.vynils.ui.viewmodel.CollectorListScreenViewModel

@Composable
fun CollectorListScreen(
    viewModel: CollectorListScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadCollectors()
    }
    CollectorList(state.collectors)
}