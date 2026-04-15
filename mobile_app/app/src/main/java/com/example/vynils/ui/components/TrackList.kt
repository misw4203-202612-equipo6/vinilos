package com.example.vynils.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.vynils.model.Track

@Composable
fun TrackList(tracks: List<Track>){
    LazyColumn() {
        items(tracks) { track ->
            TrackListElement(track)
        }
    }
}