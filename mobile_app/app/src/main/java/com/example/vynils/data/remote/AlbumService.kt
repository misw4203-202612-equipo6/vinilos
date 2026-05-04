package com.example.vynils.data.remote

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path
import com.example.vynils.model.Album
import com.example.vynils.model.Track
import com.example.vynils.model.CreateTrackRequest
import com.example.vynils.model.AlbumRequest

interface AlbumService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") id: Int): Album

    @POST("albums/{id}/tracks")
    suspend fun addTrackToAlbum(@Path("id") albumId: Int, @Body track: CreateTrackRequest): Track

    @POST("albums")
    suspend fun createAlbum(@Body album: AlbumRequest): Album
}