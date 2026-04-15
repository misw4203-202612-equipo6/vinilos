package com.example.vynils.model

import com.google.gson.annotations.SerializedName

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    @SerializedName("performers")
    val artists: List<Artist>? = emptyList()
)