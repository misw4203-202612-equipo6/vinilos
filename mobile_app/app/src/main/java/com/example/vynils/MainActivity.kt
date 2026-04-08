package com.example.vynils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.vynils.ui.AppNavigation
import com.example.vynils.ui.theme.VynilsTheme
import com.example.vynils.ui.layout.Header
import com.example.vynils.ui.layout.Toaster

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilsTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { Header(navController) },
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
    }
}