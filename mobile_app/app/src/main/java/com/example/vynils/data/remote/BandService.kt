package com.example.vynils.data.remote

import com.example.vynils.model.Band
import retrofit2.http.GET
import retrofit2.http.Path

interface BandService {
    @GET("bands")
    suspend fun getBands(): List<Band>

    @GET("bands/{id}")
    suspend fun getBand(@Path("id") id: Int): Band
}
