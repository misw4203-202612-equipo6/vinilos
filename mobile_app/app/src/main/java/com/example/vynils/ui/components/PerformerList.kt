package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vynils.model.Performer

@Composable
fun PerformerList(
    performers: List<Performer>,
    onPerformerClick: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier,
    isLazy: Boolean = true,
    tagPrefix: String = "performer",
    showDetailButton: Boolean = false,
    showDescription: Boolean = false
) {
    if (isLazy) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(performers) { performer ->
                PerformerListElement(
                    performer = performer,
                    onClick = onPerformerClick?.let { click -> { click(performer.id) } },
                    tagPrefix = tagPrefix,
                    showDetailButton = showDetailButton,
                    showDescription = showDescription
                )
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        }
    } else {
        Column(modifier = modifier) {
            performers.forEach { performer ->
                PerformerListElement(
                    performer = performer,
                    onClick = onPerformerClick?.let { click -> { click(performer.id) } },
                    tagPrefix = tagPrefix,
                    showDetailButton = showDetailButton,
                    showDescription = showDescription
                )
                HorizontalDivider(color = Color(0xFFD5D9E0))
            }
        }
    }
}
