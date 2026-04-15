package com.example.vynils.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.vynils.model.Performer

@Composable
fun MusicianList(musicians: List<Performer>){
    LazyColumn() {
        items(musicians) { musician ->
            MusicianListElement(musician)
        }
    }
}