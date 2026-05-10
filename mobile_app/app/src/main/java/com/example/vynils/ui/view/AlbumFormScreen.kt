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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.R
import com.example.vynils.ui.viewmodel.AlbumArtistOption
import com.example.vynils.ui.viewmodel.AlbumFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumFormScreen(
    navController: NavController,
    viewModel: AlbumFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var cover by remember { mutableStateOf("") }
    var releaseDate by remember { mutableStateOf("") }
    var recordLabel by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedArtist by remember { mutableStateOf<AlbumArtistOption?>(null) }
    var expandedArtists by remember { mutableStateOf(false) }
    var expandedGenres by remember { mutableStateOf(false) }
    var expandedLabels by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val genres = listOf("Rock", "Salsa", "Classical", "Folk")
    val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")
    val requiredFieldsMessage = stringResource(id = R.string.msg_album_required_fields)

    LaunchedEffect(state.success) {
        if (state.success) {
            validationError = null
            navController.previousBackStackEntry?.savedStateHandle?.set("refreshAlbums", true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_add_album)) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .testTag("album-create-form")
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    validationError = null
                },
                label = { Text(stringResource(id = R.string.label_album_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("album-create-name"),
                singleLine = true,
                colors = defaultFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedArtists,
                onExpandedChange = { expandedArtists = !expandedArtists }
            ) {
                OutlinedTextField(
                    value = selectedArtist?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.label_artist_required)) },
                    placeholder = { Text(stringResource(id = R.string.placeholder_select_artist)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedArtists) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("album-create-artist"),
                    colors = defaultFieldColors()
                )
                DropdownMenu(
                    expanded = expandedArtists,
                    onDismissRequest = { expandedArtists = false }
                ) {
                    state.artists.forEach { artist ->
                        DropdownMenuItem(
                            text = { Text(artist.name) },
                            onClick = {
                                selectedArtist = artist
                                expandedArtists = false
                                validationError = null
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter(Char::isDigit).take(4) },
                label = { Text(stringResource(id = R.string.label_year_optional)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("album-create-year"),
                singleLine = true,
                colors = defaultFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedGenres,
                onExpandedChange = { expandedGenres = !expandedGenres }
            ) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.label_genre_optional)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenres) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("album-create-genre"),
                    colors = defaultFieldColors()
                )
                DropdownMenu(
                    expanded = expandedGenres,
                    onDismissRequest = { expandedGenres = false }
                ) {
                    genres.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                genre = option
                                expandedGenres = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cover,
                onValueChange = { cover = it },
                label = { Text(stringResource(id = R.string.label_album_cover_url)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("album-create-cover"),
                singleLine = true,
                colors = defaultFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = releaseDate,
                onValueChange = { releaseDate = it },
                label = { Text(stringResource(id = R.string.label_album_release_date)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("album-create-release-date"),
                singleLine = true,
                colors = defaultFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedLabels,
                onExpandedChange = { expandedLabels = !expandedLabels }
            ) {
                OutlinedTextField(
                    value = recordLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.label_album_record_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLabels) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("album-create-record-label"),
                    colors = defaultFieldColors()
                )
                DropdownMenu(
                    expanded = expandedLabels,
                    onDismissRequest = { expandedLabels = false }
                ) {
                    recordLabels.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                recordLabel = option
                                expandedLabels = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(id = R.string.label_album_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .testTag("album-create-description"),
                maxLines = 5,
                colors = defaultFieldColors()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.loading || state.loadingArtists) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                state.error?.let {
                    Text(
                        text = stringResource(id = R.string.error_prefix, it),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .testTag("album-create-error")
                    )
                }

                validationError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .testTag("album-create-validation-error")
                    )
                }

                state.successMessage?.let {
                    Text(
                        text = it,
                        color = Color(0xFF1B5E20),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .testTag("album-create-success")
                    )
                }

                Button(
                    onClick = {
                        when {
                            name.isBlank() || selectedArtist == null -> {
                                validationError = requiredFieldsMessage
                            }
                            else -> {
                                viewModel.createAlbum(
                                    name = name,
                                    selectedArtist = selectedArtist!!,
                                    year = year,
                                    genre = genre,
                                    cover = cover,
                                    releaseDate = releaseDate,
                                    description = description,
                                    recordLabel = recordLabel
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("album-create-save"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(id = R.string.btn_save))
                }

                TextButton(
                    onClick = {
                        viewModel.resetState()
                        val returned = navController.popBackStack("albums", inclusive = false)
                        if (!returned) {
                            navController.navigate("albums") {
                                popUpTo("albumForm") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("album-create-cancel")
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_cancel),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun defaultFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.Black,
    focusedLabelColor = Color.Black,
    cursorColor = Color.Black
)
