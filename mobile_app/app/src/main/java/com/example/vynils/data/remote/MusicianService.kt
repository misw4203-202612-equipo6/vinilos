package com.example.vynils.data.remote

import com.example.vynils.model.Musician
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicianService {
    @GET("musicians")
    suspend fun getMusicians(): List<Musician>

    @GET("musicians/{id}")
    suspend fun getMusician(@Path("id") id: Int): Musician
}
