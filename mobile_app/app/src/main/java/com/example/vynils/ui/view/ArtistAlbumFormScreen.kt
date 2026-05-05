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
import com.example.vynils.ui.viewmodel.AlbumOption
import com.example.vynils.ui.viewmodel.ArtistAlbumFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistAlbumFormScreen(
    navController: NavController,
    artistId: Int,
    artistType: String,
    viewModel: ArtistAlbumFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(artistId, artistType) {
        viewModel.loadAlbums(artistId, artistType)
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.previousBackStackEntry?.savedStateHandle?.set("refreshArtist", true)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_add_album_to_artist)) },
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
                        value = state.selectedAlbum?.let { albumLabel(it) }
                            ?: stringResource(id = R.string.placeholder_select_album),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.label_select_album)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("artist-album-dropdown-field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        trailingIcon = {
                            TextButton(
                                onClick = { expanded = !expanded },
                                enabled = state.albums.isNotEmpty(),
                                modifier = Modifier.testTag("artist-album-dropdown-toggle")
                            ) {
                                Text(if (expanded) "▲" else "▼", color = Color.Black)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded && state.albums.isNotEmpty(),
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        state.albums.forEach { album ->
                            DropdownMenuItem(
                                text = { Text(albumLabel(album)) },
                                onClick = {
                                    viewModel.selectAlbum(album)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (state.albums.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.msg_all_albums_assigned),
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
                onClick = { viewModel.saveAlbum(artistId, artistType) },
                enabled = state.selectedAlbum != null && !state.saving && !state.loading,
                modifier = Modifier.fillMaxWidth()
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
            ) {
                Text(
                    text = stringResource(id = R.string.btn_back),
                    color = Color.Black
                )
            }
        }
    }
}

private fun albumLabel(album: AlbumOption): String = album.name
