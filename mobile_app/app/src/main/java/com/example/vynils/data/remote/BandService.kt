package com.example.vynils.data.remote

import com.example.vynils.model.Band
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response

interface BandService {
    @GET("bands")
    suspend fun getBands(): List<Band>

    @GET("bands/{id}")
    suspend fun getBand(@Path("id") id: Int): Band

    @POST("bands/{bandId}/albums/{albumId}")
    suspend fun addAlbumToBand(
        @Path("bandId") bandId: Int,
        @Path("albumId") albumId: Int
    ): Response<Unit>
}
