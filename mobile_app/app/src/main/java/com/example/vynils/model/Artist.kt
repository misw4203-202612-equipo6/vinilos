package com.example.vynils.model

data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val collectors: List<Collector> = emptyList(),
    val albums: List<Album> = emptyList(),
    val musicians: List<Musician> = emptyList()
)