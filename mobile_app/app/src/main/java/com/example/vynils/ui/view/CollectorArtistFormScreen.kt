package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.R
import com.example.vynils.ui.viewmodel.ArtistOption
import com.example.vynils.ui.viewmodel.ArtistType
import com.example.vynils.ui.viewmodel.CollectorArtistFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorArtistFormScreen(
    navController: NavController,
    collectorId: Int,
    viewModel: CollectorArtistFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(collectorId) {
        viewModel.loadArtists(collectorId)
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.previousBackStackEntry?.savedStateHandle?.set("refreshCollector", true)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_add_favorite_artist)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.desc_back)
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
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (state.loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.selectedArtist?.let { collectorArtistLabel(it) }
                            ?: stringResource(id = R.string.placeholder_select_artist),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.label_select_artist)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("collector-artist-dropdown-field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        trailingIcon = {
                            TextButton(
                                onClick = { expanded = !expanded },
                                enabled = state.artists.isNotEmpty(),
                                modifier = Modifier.testTag("collector-artist-dropdown-toggle")
                            ) {
                                Text(if (expanded) "▲" else "▼", color = Color.Black)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded && state.artists.isNotEmpty(),
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        state.artists.forEach { artist ->
                            DropdownMenuItem(
                                text = { Text(collectorArtistLabel(artist)) },
                                onClick = {
                                    viewModel.selectArtist(artist)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (state.artists.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.msg_all_favorite_artists_assigned),
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.validationError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            state.error?.let {
                Text(
                    text = stringResource(id = R.string.error_prefix, it),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.saveArtist(collectorId) },
                enabled = state.selectedArtist != null && !state.saving && !state.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("collector-artist-save-button")
            ) {
                if (state.saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(stringResource(id = R.string.btn_save))
                }
            }

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("collector-artist-cancel-button")
            ) {
                Text(
                    text = stringResource(id = R.string.btn_cancel),
                    color = Color.Black
                )
            }
        }
    }
}

private fun collectorArtistLabel(artist: ArtistOption): String {
    return when (artist.type) {
        ArtistType.BAND -> "${artist.name} (Banda)"
        ArtistType.MUSICIAN -> "${artist.name} (Músico)"
    }
}
