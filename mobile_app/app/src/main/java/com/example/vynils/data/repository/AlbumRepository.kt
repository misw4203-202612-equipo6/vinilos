package com.example.vynils.data.repository

import com.example.vynils.data.remote.AlbumService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Album
import com.example.vynils.model.AlbumRequest

class AlbumRepository(
    private val service: AlbumService = RetrofitInstance.albumService
) {
    suspend fun getAlbums(): List<Album> {
        return service.getAlbums()
    }

    suspend fun getAlbum(id: Int): Album {
        return service.getAlbum(id)
    }

    suspend fun createAlbum(album: AlbumRequest): Album {
        return service.createAlbum(album)
    }
}