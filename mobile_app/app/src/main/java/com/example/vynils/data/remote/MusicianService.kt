package com.example.vynils.data.remote

import com.example.vynils.model.Musician
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response

interface MusicianService {
    @GET("musicians")
    suspend fun getMusicians(): List<Musician>

    @GET("musicians/{id}")
    suspend fun getMusician(@Path("id") id: Int): Musician

    @POST("musicians/{musicianId}/albums/{albumId}")
    suspend fun addAlbumToMusician(
        @Path("musicianId") musicianId: Int,
        @Path("albumId") albumId: Int
    ): Response<Unit>
}
