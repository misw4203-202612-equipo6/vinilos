package com.example.vynils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vynils.ui.AppNavigation
import com.example.vynils.ui.theme.VynilsTheme
import com.example.vynils.ui.layout.Header
import com.example.vynils.ui.layout.Toaster
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilsTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerContentColor = Color.Black
            ) {
                Text(
                    stringResource(id = R.string.title_vinilos),
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(stringResource(id = R.string.nav_home)) },
                    selected = currentRoute == "home",
                    onClick = {
                        navController.navigate("home")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.LightGray,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = R.string.nav_albums)) },
                    selected = currentRoute == "albums",
                    onClick = {
                        navController.navigate("albums")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Album, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.LightGray,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = R.string.nav_artists)) },
                    selected = currentRoute == "artists",
                    onClick = {
                        navController.navigate("artists")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.LightGray,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(id = R.string.nav_collectors)) },
                    selected = currentRoute == "collectors",
                    onClick = {
                        navController.navigate("collectors")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Group, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.LightGray,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black
                    )
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            snackbarHost = { Toaster() },
            content = { padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    AppNavigation(navController)
                }
            }
        )
    }
}
