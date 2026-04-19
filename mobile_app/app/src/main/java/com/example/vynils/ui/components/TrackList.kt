package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.vynils.model.Track

@Composable
fun TrackList(tracks: List<Track>) {
    Column {
        tracks.forEach { track ->
            TrackListElement(track)
        }
    }
}
