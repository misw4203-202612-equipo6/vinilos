package com.example.vynils.model

data class PerformerItem(
    override val id: Int,
    override val name: String,
    override val image: String,
    override val description: String,
    val birthDate: String? = null,
    val creationDate: String? = null
) : Performer
