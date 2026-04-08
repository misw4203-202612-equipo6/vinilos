package com.example.vynils.data.remote

import retrofit2.http.GET
import com.example.vynils.model.Album

interface AlbumService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>
}