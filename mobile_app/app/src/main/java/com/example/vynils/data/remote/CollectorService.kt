package com.example.vynils.data.remote

import com.example.vynils.model.Collector
import retrofit2.http.GET
import retrofit2.http.Path

interface CollectorService {
    @GET("collectors")
    suspend fun getCollectors(): List<Collector>

    @GET("collectors/{id}")
    suspend fun getCollector(@Path("id") id: Int): Collector
}