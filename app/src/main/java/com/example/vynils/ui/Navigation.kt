package com.example.vynils.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vynils.ui.view.Home
import com.example.vynils.ui.view.AlbumListScreen
import com.example.vynils.ui.view.PerformerListScreen
import com.example.vynils.ui.view.CollectorListScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home(navController)
        }
        composable("albums") {
            AlbumListScreen()
        }
        composable("artists") {
            PerformerListScreen()
        }
        composable("collectors") {
            CollectorListScreen()
        }
    }
}