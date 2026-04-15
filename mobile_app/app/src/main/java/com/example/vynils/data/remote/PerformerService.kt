package com.example.vynils.data.remote

import com.example.vynils.model.Album
import com.example.vynils.model.Performer
import retrofit2.http.GET
import retrofit2.http.Path

interface PerformerService {
    @GET("bands")
    suspend fun getPerformers(): List<Performer>

    @GET("bands/{id}")
    suspend fun getPerformer(@Path("id") id: Int): Performer
}