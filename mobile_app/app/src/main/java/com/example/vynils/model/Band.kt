package com.example.vynils.model

data class Band(
    override val id: Int,
    override val name: String,
    override val image: String,
    override val description: String,
    val creationDate: String? = null,
    val collectors: List<Collector> = emptyList(),
    val albums: List<Album> = emptyList(),
    val musicians: List<Musician> = emptyList()
) : Performer
