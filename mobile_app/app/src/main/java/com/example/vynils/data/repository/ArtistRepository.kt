package com.example.vynils.data.repository

import com.example.vynils.data.remote.ArtistService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Artist

class ArtistRepository(
    private val service: ArtistService = RetrofitInstance.artistService
) {
    suspend fun getArtists(): List<Artist> {
        return service.getArtists()
    }

    suspend fun getArtist(id: Int): Artist {
        return service.getArtist(id)
    }
}