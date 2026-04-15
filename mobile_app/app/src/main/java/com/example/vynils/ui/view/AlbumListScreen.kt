package com.example.vynils.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynils.R
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.ui.components.AlbumList
import com.example.vynils.ui.viewmodel.AlbumViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListScreen(
    navController: NavController,
    viewModel: AlbumViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showFilterSheet by remember { mutableStateOf(false) }

    // Estados temporales para el filtro en el BottomSheet
    var tempName by remember { mutableStateOf("") }
    var tempGenre by remember { mutableStateOf("") }
    var tempYear by remember { mutableStateOf("") }

    // Sincronizar estados temporales cuando se abre el sheet
    LaunchedEffect(showFilterSheet) {
        if (showFilterSheet) {
            tempName = state.filterName
            tempGenre = state.filterGenre
            tempYear = state.filterYear
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadAlbums()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.title_albums),
                modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )


            IconButton(
                onClick = { showFilterSheet = true },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                BadgedBox(badge = {
                    if (state.filterName.isNotEmpty() || state.filterGenre.isNotEmpty() || state.filterYear.isNotEmpty()) {
                        Badge(containerColor = Color.Black) { Text("", color = Color.White) }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(id = R.string.desc_filter),
                        tint = Color.Black
                    )
                }
            }

        }

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else {
            AlbumList(state.filteredAlbums, onAlbumClick = { albumId ->
                navController.navigate("albumDetail/$albumId")
            })
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        stringResource(id = R.string.filter_title_albums),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text(stringResource(id = R.string.filter_label_album_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tempGenre,
                        onValueChange = { tempGenre = it },
                        label = { Text(stringResource(id = R.string.filter_label_genre)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tempYear,
                        onValueChange = { tempYear = it },
                        label = { Text(stringResource(id = R.string.filter_label_year)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.clearFilters()
                                showFilterSheet = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            ),
                            border = BorderStroke(1.dp, Color.Black)
                        ) {
                            Text(stringResource(id = R.string.btn_clear))
                        }
                        Button(
                            onClick = {
                                viewModel.updateFilters(tempName, tempGenre, tempYear)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) showFilterSheet = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(id = R.string.btn_apply))
                        }
                    }
                }
            }
        }
    }
}