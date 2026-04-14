package com.example.vynils.data.remote

import com.example.vynils.model.Album
import com.example.vynils.model.Performer
import retrofit2.http.GET
import retrofit2.http.Path

interface PerformerService {
    @GET("performers")
    suspend fun getPerformers(): List<Performer>

    @GET("performers/{id}")
    suspend fun getPerformer(@Path("id") id: Int): Performer
}