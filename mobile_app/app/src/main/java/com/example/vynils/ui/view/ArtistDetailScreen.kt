package com.example.vynils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynils.R
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vynils.ui.components.AlbumListElement
import com.example.vynils.ui.components.PerformerList
import com.example.vynils.ui.components.TrackList
import com.example.vynils.model.Band
import com.example.vynils.model.Musician
import com.example.vynils.ui.viewmodel.PerformerDetailScreenViewModel

@Composable
fun ArtistDetailScreen(
    navController: NavController,
    artistId: Int,
    viewModel: PerformerDetailScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadPerformer(artistId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("artist-detail-scroll")
            .verticalScroll(rememberScrollState())
    ) {
        when {
            state.error != null -> {
                Text(
                    text = stringResource(id = R.string.error_prefix, state.error ?: ""),
                    modifier = Modifier.padding(20.dp),
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }

            state.loading || state.performer == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            else -> {
                val performer = state.performer
                val artistImage = performer?.image?.let {
                    if (it.startsWith("http://")) it.replace("http://", "https://") else it
                } ?: ""

                AsyncImage(
                    model = if (artistImage.isEmpty()) null else artistImage,
                    contentDescription = stringResource(id = R.string.desc_artist_image, performer?.name ?: ""),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(width = 248.dp, height = 175.dp)
                        .testTag("artist-detail-image")
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.no_image),
                    error = painterResource(id = R.drawable.no_image)
                )

                Text(
                    text = performer?.name ?: stringResource(id = R.string.loading),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .testTag("artist-detail-name"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                ArtistDetailSectionHeader(
                    title = stringResource(id = R.string.label_biography)
                )
                Text(
                    text = performer?.description ?: "",
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .testTag("artist-detail-biography"),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify
                )

                ArtistDetailAlbumsHeader()

                val albums = when (performer) {
                    is Band -> performer.albums
                    is Musician -> performer.albums
                    else -> emptyList()
                }
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
                        text = stringResource(id = R.string.empty_albums),
                        modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "Tracks",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 24.dp, bottom = 10.dp)
                        .testTag("artist-tracks-title"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (state.tracks.isNotEmpty()) {
                    TrackList(tracks = state.tracks)
                } else {
                    Text(
                        text = "No hay tracks disponibles",
                        modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                if (performer is Band) {
                    Text(
                        text = stringResource(id = R.string.label_musicians),
                        modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val musicians = performer.musicians
                    if (musicians.isNotEmpty()) {
                        PerformerList(
                            performers = musicians,
                            isLazy = false,
                            tagPrefix = "musician",
                            showDescription = true
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.empty_musicians),
                            modifier = Modifier.padding(start = 20.dp, top = 4.dp),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ArtistDetailSectionHeader(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ArtistDetailAlbumsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 12.dp, end = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.nav_albums),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        /*
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Agregar album",
                tint = Color.Black
            )
        }
        */
    }
}
