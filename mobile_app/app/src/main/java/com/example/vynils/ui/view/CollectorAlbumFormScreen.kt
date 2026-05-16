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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.R
import com.example.vynils.ui.viewmodel.AlbumOption
import com.example.vynils.ui.viewmodel.CollectorAlbumFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorAlbumFormScreen(
    navController: NavController,
    collectorId: Int,
    viewModel: CollectorAlbumFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var albumExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(collectorId) {
        viewModel.loadAlbums(collectorId)
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
                title = { Text(stringResource(id = R.string.title_add_album_to_collector)) },
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
                // Album dropdown
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.selectedAlbum?.name
                            ?: stringResource(id = R.string.placeholder_select_album),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.label_select_album)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("collector-album-dropdown-field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        trailingIcon = {
                            TextButton(
                                onClick = { albumExpanded = !albumExpanded },
                                enabled = state.albums.isNotEmpty(),
                                modifier = Modifier.testTag("collector-album-dropdown-toggle")
                            ) {
                                Text(if (albumExpanded) "▲" else "▼", color = Color.Black)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = albumExpanded && state.albums.isNotEmpty(),
                        onDismissRequest = { albumExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        state.albums.forEach { album ->
                            DropdownMenuItem(
                                text = { Text(album.name) },
                                onClick = {
                                    viewModel.selectAlbum(album)
                                    albumExpanded = false
                                }
                            )
                        }
                    }
                }

                if (state.albums.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.msg_all_collector_albums_assigned),
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price field
                OutlinedTextField(
                    value = state.price,
                    onValueChange = { viewModel.setPrice(it) },
                    label = { Text(stringResource(id = R.string.label_album_price)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("collector-album-price-field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Status dropdown
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.status,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.label_album_status)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("collector-album-status-field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        trailingIcon = {
                            TextButton(
                                onClick = { statusExpanded = !statusExpanded },
                                modifier = Modifier.testTag("collector-album-status-toggle")
                            ) {
                                Text(if (statusExpanded) "▲" else "▼", color = Color.Black)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.status_active)) },
                            onClick = {
                                viewModel.setStatus("Active")
                                statusExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.status_inactive)) },
                            onClick = {
                                viewModel.setStatus("Inactive")
                                statusExpanded = false
                            }
                        )
                    }
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
                onClick = { viewModel.saveAlbum(collectorId) },
                enabled = state.selectedAlbum != null && !state.saving && !state.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("collector-album-save-button")
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
                    .testTag("collector-album-cancel-button")
            ) {
                Text(
                    text = stringResource(id = R.string.btn_cancel),
                    color = Color.Black
                )
            }
        }
    }
}
