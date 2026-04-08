package com.example.vynils.ui.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynils.ui.viewmodel.CollectorListScreenViewModel

@Composable
fun CollectorListScreen(
    viewModel: CollectorListScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadCollectors()
    }
    LazyColumn() {
        items(state.collectors) { collector ->
            Text(collector.name)
        }
    }
}