package com.example.vynils.model

data class Collector(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val favoritePerformers: List<Artist>,
    val comments: List<Comment>,
    val collectorAlbums: List<Album>
)