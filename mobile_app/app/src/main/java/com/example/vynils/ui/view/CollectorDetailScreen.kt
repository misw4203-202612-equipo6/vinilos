package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import com.example.vynils.R
import androidx.compose.ui.res.stringResource
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
import com.example.vynils.ui.components.AlbumList
import com.example.vynils.ui.components.ArtistList
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
    } else if (state.error != null) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.error_prefix, state.error ?: ""),
                color = Color.Red,
                modifier = Modifier.padding(20.dp)
            )
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
            contentDescription = stringResource(id = R.string.desc_collector_image),
            modifier = Modifier
                .padding(top = 20.dp)
                .size(width = 248.dp, height = 175.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.no_image),
            error = painterResource(id = R.drawable.no_image)
        )
        Text(
            text = state.collector?.name ?: stringResource(id = R.string.loading),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.label_phone, state.collector?.telephone ?: ""),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
            fontSize = 14.sp
        )
        Text(
            text = stringResource(id = R.string.label_email, state.collector?.email ?: ""),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 2.dp, bottom = 10.dp),
            fontSize = 14.sp
        )

        Text(
            text = stringResource(id = R.string.label_albums_in_collection),
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        val collectorAlbums = state.collector?.collectorAlbums ?: emptyList()
        if (collectorAlbums.isNotEmpty()) {
            AlbumList(
                albums = collectorAlbums,
                onAlbumClick = { navController.navigate("albumDetail/$it") },
                isLazy = false
            )
        } else {
            Text(
                text = stringResource(id = R.string.empty_collection),
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = stringResource(id = R.string.label_favorite_artists),
            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        val favoritePerformers = state.collector?.favoritePerformers ?: emptyList()
        if (favoritePerformers.isNotEmpty()) {
            ArtistList(
                artists = favoritePerformers,
                onArtistClick = { navController.navigate("artistDetail/$it") },
                isLazy = false
            )
        } else {
            Text(
                text = stringResource(id = R.string.empty_favorites),
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}