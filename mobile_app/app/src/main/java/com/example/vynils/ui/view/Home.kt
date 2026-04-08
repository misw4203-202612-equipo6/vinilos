package com.example.vynils.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController) {
    Column() {
        Button(onClick = {
            navController.navigate("albums")
        }) {
            Text("Albums")
        }
        Button(onClick = {
            navController.navigate("artists")
        }) {
            Text("Artistas")
        }
        Button(onClick = {
            navController.navigate("collectors")
        }) {
            Text("Coleccionistas")
        }
    }
}