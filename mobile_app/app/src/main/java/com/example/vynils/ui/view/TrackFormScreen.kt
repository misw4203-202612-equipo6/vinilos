package com.example.vynils.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.ui.viewmodel.TrackDraft
import com.example.vynils.ui.viewmodel.TrackFormScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackFormScreen(
    navController: NavController,
    albumId: Int,
    viewModel: TrackFormScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.previousBackStackEntry?.savedStateHandle?.set("refreshAlbum", true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Tracks") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearState()
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .testTag("track-form-root"),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track-form-name"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.duration,
                onValueChange = { viewModel.updateDuration(it) },
                label = { Text("Duración") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track-form-duration"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.addOrUpdateTrack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track-form-add")
            ) {
                Text(if (state.editingTrackId == null) "Agregar Track" else "Actualizar Track")
            }

            Spacer(modifier = Modifier.height(12.dp))

            state.validationError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("track-form-validation-error")
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            state.error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("track-form-error")
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            state.successMessage?.let {
                Text(
                    text = it,
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.testTag("track-form-success")
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text("Tracks agregados")

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("track-form-list")
            ) {
                items(state.tracks, key = { it.localId }) { track ->
                    TrackDraftRow(
                        track = track,
                        onEdit = { viewModel.editTrack(track.localId) },
                        onDelete = { viewModel.deleteTrack(track.localId) }
                    )
                }
            }

            if (state.loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.saveTracks(albumId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("track-form-save")
                ) {
                    Text("Guardar")
                }
            }

            TextButton(
                onClick = {
                    viewModel.clearState()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track-form-cancel")
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
private fun TrackDraftRow(
    track: TrackDraft,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("track-item"),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(track.name)
            Text(track.duration)
        }
        IconButton(
            onClick = onEdit,
            modifier = Modifier.testTag("track-item-edit")
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Editar track")
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.testTag("track-item-delete")
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar track")
        }
    }
}
