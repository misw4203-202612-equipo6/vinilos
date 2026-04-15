package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.vynils.ui.components.AlbumListElement
import com.example.vynils.ui.viewmodel.ArtistDetailScreenViewModel

@Composable
fun ArtistDetailScreen(
    navController: NavController,
    artistId: Int,
    viewModel: ArtistDetailScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(artistId) {
        viewModel.loadArtist(artistId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        val artistImage = state.artist?.image?.let {
            if (it.startsWith("http://")) it.replace("http://", "https://") else it
        } ?: ""

        AsyncImage(
            model = if (artistImage.isEmpty()) null else artistImage,
            contentDescription = "Imagen de ${state.artist?.name}",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(20.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.no_image),
            error = painterResource(id = R.drawable.no_image)
        )
        Text(
            text = state.artist?.name ?: "Cargando...",
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Biografía",
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = state.artist?.description ?: "",
            modifier = Modifier.padding(start = 20.dp, top = 4.dp, bottom = 10.dp),
            fontSize = 14.sp
        )
        
        Text(
            text = "Álbumes",
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        val albums = state.artist?.albums ?: emptyList()
        if (albums.isNotEmpty()) {
            albums.forEach { album ->
                AlbumListElement(
                    album = album,
                    onClick = { navController.navigate("albumDetail/${album.id}") }
                )
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        } else {
            Text(
                text = "No hay álbumes disponibles",
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}