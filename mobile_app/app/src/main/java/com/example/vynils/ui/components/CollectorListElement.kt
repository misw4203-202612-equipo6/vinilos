package com.example.vynils.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vynils.R
import com.example.vynils.model.Album
import com.example.vynils.model.Collector

@Composable
fun CollectorListElement(collector: Collector){
    Row{
        AsyncImage(
            model = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
            contentDescription = "User profile image place holder",
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.mi_imagen),
            error = painterResource(id = R.drawable.mi_imagen)
        )
        Column{
            Text(collector.name)
            Text(collector.telephone)
            Text(collector.email)
        }
    }
}