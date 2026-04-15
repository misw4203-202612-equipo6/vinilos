package com.example.vynils.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val albumService: AlbumService by lazy {
        retrofit.create(AlbumService::class.java)
    }
    val artistService: ArtistService by lazy {
        retrofit.create(ArtistService::class.java)
    }
    val collectorService: CollectorService by lazy {
        retrofit.create(CollectorService::class.java)
    }
}