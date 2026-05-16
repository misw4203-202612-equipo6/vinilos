package com.example.vynils.data.remote

import com.example.vynils.model.Collector
import com.example.vynils.model.CollectorAlbumRequest
import com.example.vynils.model.PerformerItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectorService {
    @GET("collectors")
    suspend fun getCollectors(): List<Collector>

    @GET("collectors/{id}")
    suspend fun getCollector(@Path("id") id: Int): Collector

    @GET("collectors/{collectorId}/performers")
    suspend fun getPerformersByCollector(@Path("collectorId") collectorId: Int): List<PerformerItem>

    @POST("collectors/{collectorId}/bands/{bandId}")
    suspend fun addBandToCollector(
        @Path("collectorId") collectorId: Int,
        @Path("bandId") bandId: Int
    ): Response<Unit>

    @POST("collectors/{collectorId}/musicians/{musicianId}")
    suspend fun addMusicianToCollector(
        @Path("collectorId") collectorId: Int,
        @Path("musicianId") musicianId: Int
    ): Response<Unit>

    @POST("collectors/{collectorId}/albums/{albumId}")
    suspend fun addAlbumToCollector(
        @Path("collectorId") collectorId: Int,
        @Path("albumId") albumId: Int,
        @Body request: CollectorAlbumRequest
    ): Response<Unit>
}