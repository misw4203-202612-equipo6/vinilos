package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.ui.components.AlbumList
import com.example.vynils.ui.viewmodel.CollectorDetailScreenViewModel

@Composable
fun CollectorDetailScreen(
    navController: NavController,
    collectorId: Int,
    viewModel: CollectorDetailScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(collectorId) {
        viewModel.loadCollector(collectorId)
    }

    if (state.loading) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Black)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
        AsyncImage(
            model = R.drawable.no_image,
            contentDescription = "Imagen del coleccionista",
            modifier = Modifier
                .padding(20.dp)
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.no_image),
            error = painterResource(id = R.drawable.no_image)
        )
        Text(
            text = state.collector?.name ?: "Cargando...",
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Información de contacto",
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 4.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Teléfono: ${state.collector?.telephone ?: ""}",
            modifier = Modifier.padding(start = 20.dp, top = 4.dp, bottom = 2.dp),
            fontSize = 14.sp
        )
        Text(
            text = "Email: ${state.collector?.email ?: ""}",
            modifier = Modifier.padding(start = 20.dp, top = 2.dp, bottom = 10.dp),
            fontSize = 14.sp
        )

        Text(
            text = "Álbumes en la colección",
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        val collectorAlbums = state.collector?.collectorAlbums ?: emptyList()
        if (collectorAlbums.isNotEmpty()) {
            collectorAlbums.forEach { album ->
                com.example.vynils.ui.components.AlbumListElement(
                    album = album,
                    onClick = { navController.navigate("albumDetail/${album.id}") }
                )
                androidx.compose.material3.HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        } else {
            Text(
                text = "No hay álbumes en la colección",
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = "Artistas favoritos",
            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        val favoritePerformers = state.collector?.favoritePerformers ?: emptyList()
        if (favoritePerformers.isNotEmpty()) {
            favoritePerformers.forEach { artist ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val artistImage = artist.image?.let {
                        if (it.startsWith("http://")) it.replace("http://", "https://") else it
                    } ?: ""
                    
                    AsyncImage(
                        model = if (artistImage.isEmpty()) null else artistImage,
                        contentDescription = artist.name,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.no_image),
                        error = painterResource(id = R.drawable.no_image)
                    )
                    Text(
                        text = artist.name ?: "",
                        modifier = Modifier.padding(start = 12.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                androidx.compose.material3.HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        } else {
            Text(
                text = "No hay artistas favoritos",
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}