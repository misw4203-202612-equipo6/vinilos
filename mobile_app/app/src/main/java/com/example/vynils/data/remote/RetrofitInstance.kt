package com.example.vynils.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://vinilos-backend-equipo6-db91c0ab96d3.herokuapp.com/"
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val albumService: AlbumService by lazy {
        retrofit.create(AlbumService::class.java)
    }
    val bandService: BandService by lazy {
        retrofit.create(BandService::class.java)
    }
    val musicianService: MusicianService by lazy {
        retrofit.create(MusicianService::class.java)
    }
    val performerService: PerformerService by lazy {
        PerformerService(
            bandService = bandService,
            musicianService = musicianService
        )
    }
    val collectorService: CollectorService by lazy {
        retrofit.create(CollectorService::class.java)
    }
}
