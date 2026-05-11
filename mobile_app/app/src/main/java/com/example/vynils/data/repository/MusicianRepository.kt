package com.example.vynils.data.repository

import com.example.vynils.data.remote.MusicianService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Musician

class MusicianRepository(
    private val service: MusicianService = RetrofitInstance.musicianService
) {
    suspend fun getMusicians(forceRefresh: Boolean = false): List<Musician> {
        if (!forceRefresh) {
            cachedMusicians?.let { return it }
        }

        return service.getMusicians().also { musicians ->
            cachedMusicians = musicians
        }
    }

    suspend fun getMusician(id: Int, forceRefresh: Boolean = false): Musician {
        if (!forceRefresh) {
            cachedMusicianDetails[id]?.let { return it }
        }

        return service.getMusician(id).also { musician ->
            cachedMusicianDetails[id] = musician
        }
    }

    suspend fun addAlbumToMusician(musicianId: Int, albumId: Int) {
        val response = service.addAlbumToMusician(musicianId, albumId)
        if (!response.isSuccessful) {
            throw IllegalStateException("No fue posible asociar el album al musico")
        }
        invalidateMusician(musicianId)
        AlbumRepository.invalidateAlbum(albumId)
        PerformerRepository.invalidatePerformers()
    }

    companion object {
        private var cachedMusicians: List<Musician>? = null
        private val cachedMusicianDetails = mutableMapOf<Int, Musician>()

        fun invalidateMusicians() {
            cachedMusicians = null
            cachedMusicianDetails.clear()
        }

        fun invalidateMusician(id: Int) {
            cachedMusicians = null
            cachedMusicianDetails.remove(id)
        }
    }
}
