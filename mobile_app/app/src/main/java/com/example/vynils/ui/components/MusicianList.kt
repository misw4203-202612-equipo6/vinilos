package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.vynils.model.Musician

@Composable
fun MusicianList(musicians: List<Musician>) {
    Column {
        musicians.forEach { musician ->
            MusicianListElement(musician = musician)
            HorizontalDivider(color = Color(0xFFD5D9E0))
        }
    }
}
