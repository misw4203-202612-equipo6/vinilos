package com.example.vynils.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.vynils.ui.view.Home
import com.example.vynils.ui.view.AlbumListScreen
import com.example.vynils.ui.view.ArtistListScreen
import com.example.vynils.ui.view.CollectorListScreen
import com.example.vynils.ui.view.AlbumDetailScreen
import com.example.vynils.ui.view.ArtistDetailScreen
import com.example.vynils.ui.view.CollectorDetailScreen

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
            AlbumListScreen(navController)
        }
        composable("artists") {
            ArtistListScreen(navController)
        }
        composable("collectors") {
            CollectorListScreen(navController)
        }
        composable(
            route = "albumDetail/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.IntType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
            AlbumDetailScreen(navController, albumId)
        }
        composable(
            route = "artistDetail/{artistId}",
            arguments = listOf(navArgument("artistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getInt("artistId") ?: 0
            ArtistDetailScreen(navController, artistId)
        }
        composable(
            route = "collectorDetail/{collectorId}",
            arguments = listOf(navArgument("collectorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val collectorId = backStackEntry.arguments?.getInt("collectorId") ?: 0
            CollectorDetailScreen(navController, collectorId)
        }
    }
}