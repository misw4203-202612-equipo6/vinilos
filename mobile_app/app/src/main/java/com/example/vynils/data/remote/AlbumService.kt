package com.example.vynils.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.vynils.model.Album

interface AlbumService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") id: Int): Album
}