package com.example.vynils.model

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val collectors: List<Collector>,
    val albums: List<Album>
)