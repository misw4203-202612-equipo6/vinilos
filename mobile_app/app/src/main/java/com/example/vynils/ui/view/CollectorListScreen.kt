package com.example.vynils.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vynils.ui.components.CollectorList
import com.example.vynils.ui.viewmodel.CollectorListScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorListScreen(
    navController: NavController,
    viewModel: CollectorListScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showFilterSheet by remember { mutableStateOf(false) }

    var tempName by remember { mutableStateOf("") }

    LaunchedEffect(showFilterSheet) {
        if (showFilterSheet) {
            tempName = state.filterName
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCollectors()
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
                text = "Coleccionistas",
                modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { showFilterSheet = true },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                BadgedBox(badge = {
                    if (state.filterName.isNotEmpty()) {
                        Badge(containerColor = Color.Black) { Text("", color = Color.White) }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar",
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
            CollectorList(state.filteredCollectors, onCollectorClick = { collectorId ->
                navController.navigate("collectorDetail/$collectorId")
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
                        "Filtrar Coleccionistas",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Nombre del coleccionista") },
                        modifier = Modifier.fillMaxWidth(),
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
                            Text("Limpiar")
                        }
                        Button(
                            onClick = {
                                viewModel.updateFilter(tempName)
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
                            Text("Aplicar")
                        }
                    }
                }
            }
        }
    }
}