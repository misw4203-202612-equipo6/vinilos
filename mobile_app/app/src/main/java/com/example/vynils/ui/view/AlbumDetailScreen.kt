package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.ui.viewmodel.AlbumDetailScreenViewModel
import com.example.vynils.utils.DateUtils

@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: Int,
    viewModel: AlbumDetailScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}", color = Color.Red)
            }
        } else {
            state.album?.let { album ->
                AsyncImage(
                    model = album.cover,
                    contentDescription = "Cover of ${album.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )

                Text(
                    text = album.name,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Lanzamiento: ${DateUtils.formatRetrofitDate(album.releaseDate)}",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = "Género: ${album.genre}",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = "Sello discográfico: ${album.recordLabel}",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = album.description ?: "",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )

                if (album.artists?.isNotEmpty() == true) {
                    Text(
                        text = "Artistas",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    album.artists.forEach { artist ->
                        Text(
                            text = artist.name,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp),
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}