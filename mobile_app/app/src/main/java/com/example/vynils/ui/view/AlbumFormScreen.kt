package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.R
import com.example.vynils.ui.viewmodel.AlbumFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumFormScreen(
    navController: NavController,
    viewModel: AlbumFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var cover by remember { mutableStateOf("") }
    var releaseDate by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var recordLabel by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    var expandedGenre by remember { mutableStateOf(false) }
    var expandedLabel by remember { mutableStateOf(false) }

    val genres = listOf("Classical", "Salsa", "Rock", "Folk")
    val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_add_album)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.desc_back))
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.error != null) {
                Text(
                    text = stringResource(id = R.string.error_prefix, state.error ?: ""),
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (showError) {
                Text(
                    text = stringResource(id = R.string.msg_error_fields_required),
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.label_album_name)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cover,
                onValueChange = { cover = it },
                label = { Text(stringResource(id = R.string.label_album_cover_url)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = releaseDate,
                onValueChange = { releaseDate = it },
                label = { Text(stringResource(id = R.string.label_album_release_date)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("YYYY-MM-DD") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Género
            ExposedDropdownMenuBox(
                expanded = expandedGenre,
                onExpandedChange = { expandedGenre = !expandedGenre },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.label_album_genre)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenre) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedGenre,
                    onDismissRequest = { expandedGenre = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    genres.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                genre = selectionOption
                                expandedGenre = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Sello Discográfico
            ExposedDropdownMenuBox(
                expanded = expandedLabel,
                onExpandedChange = { expandedLabel = !expandedLabel },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = recordLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.label_album_record_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLabel) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedLabel,
                    onDismissRequest = { expandedLabel = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    recordLabels.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                recordLabel = selectionOption
                                expandedLabel = false
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
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.loading) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                Button(
                    onClick = {
                        if (name.isBlank() || cover.isBlank() || releaseDate.isBlank() || genre.isBlank() || recordLabel.isBlank() || description.isBlank()) {
                            showError = true
                        } else {
                            showError = false
                            viewModel.createAlbum(name, cover, releaseDate, description, genre, recordLabel)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(id = R.string.btn_create_album), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}