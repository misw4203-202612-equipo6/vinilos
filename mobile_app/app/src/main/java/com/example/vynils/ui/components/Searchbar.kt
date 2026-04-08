package com.example.vynils.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.TextField
import androidx.compose.material3.Text

//@Composable
//fun SearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit
//) {
//    TextField(
//        value = query,
//        onValueChange = onQueryChange,
//        placeholder = { Text("Buscar...") },
//        singleLine = true
//    )
//}

@Composable
fun SearchBar(
) {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Buscar...") },
        singleLine = true
    )
}