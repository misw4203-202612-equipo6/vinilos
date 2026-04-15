package com.example.vynils.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.vynils.model.Collector

@Composable
fun CollectorList(collectors: List<Collector>){
    LazyColumn() {
        items(collectors) { collector ->
            CollectorListElement(collector)
        }
    }
}