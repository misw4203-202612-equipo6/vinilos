package com.example.vynils.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vynils.model.Collector

@Composable
fun CollectorList(collectors: List<Collector>, onCollectorClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(collectors) { collector ->
            CollectorListElement(collector, onClick = { onCollectorClick(collector.id) })
            HorizontalDivider(color = Color(0xFFD5D9E0))
        }
    }
}