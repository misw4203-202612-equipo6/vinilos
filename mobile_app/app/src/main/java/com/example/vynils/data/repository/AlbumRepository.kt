package com.example.vynils.data.repository

import com.example.vynils.data.remote.AlbumService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Album
import com.example.vynils.model.AlbumRequest
import com.example.vynils.model.CreateTrackRequest
import com.example.vynils.model.Track

class AlbumRepository(
    private val service: AlbumService = RetrofitInstance.albumService
) {
    suspend fun getAlbums(forceRefresh: Boolean = false): List<Album> {
        if (!forceRefresh) {
            cachedAlbums?.let { return it }
        }

        return service.getAlbums().also { albums ->
            cachedAlbums = albums
        }
    }

    suspend fun getAlbum(id: Int, forceRefresh: Boolean = false): Album {
        if (!forceRefresh) {
            cachedAlbumDetails[id]?.let { return it }
        }

        return service.getAlbum(id).also { album ->
            cachedAlbumDetails[id] = album
        }
    }

    suspend fun createAlbum(album: AlbumRequest): Album {
        return service.createAlbum(album).also {
            invalidateAlbums()
        }
    }

    suspend fun addTrackToAlbum(albumId: Int, track: CreateTrackRequest): Track {
        return service.addTrackToAlbum(albumId, track).also {
            invalidateAlbum(albumId)
        }
    }

    companion object {
        private var cachedAlbums: List<Album>? = null
        private val cachedAlbumDetails = mutableMapOf<Int, Album>()

        fun invalidateAlbums() {
            cachedAlbums = null
            cachedAlbumDetails.clear()
        }

        fun invalidateAlbum(id: Int) {
            cachedAlbums = null
            cachedAlbumDetails.remove(id)
        }
    }
}
