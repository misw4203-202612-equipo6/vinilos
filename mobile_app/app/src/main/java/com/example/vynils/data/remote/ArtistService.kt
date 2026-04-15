package com.example.vynils.data.remote

import com.example.vynils.model.Artist
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistService {
    @GET("bands")
    suspend fun getArtists(): List<Artist>

    @GET("bands/{id}")
    suspend fun getArtist(@Path("id") id: Int): Artist
}