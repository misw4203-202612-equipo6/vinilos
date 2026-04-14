package com.example.vynils.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://tu-api.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val albumService: AlbumService by lazy {
        retrofit.create(AlbumService::class.java)
    }

    val performerService: PerformerService by lazy {
        retrofit.create(PerformerService::class.java)
    }

    val collectorService: CollectorService by lazy {
        retrofit.create(CollectorService::class.java)
    }
}