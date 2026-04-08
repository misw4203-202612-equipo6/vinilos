package com.example.vynils.data.repository

import com.example.vynils.data.remote.AlbumService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Album

class AlbumRepository(
    private val service: AlbumService = RetrofitInstance.albumService
) {

    suspend fun getAlbums(): List<Album> {
        return service.getAlbums()
    }
}