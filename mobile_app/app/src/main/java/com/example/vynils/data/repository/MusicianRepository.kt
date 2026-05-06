package com.example.vynils.data.repository

import com.example.vynils.data.remote.MusicianService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Musician

class MusicianRepository(
    private val service: MusicianService = RetrofitInstance.musicianService
) {
    suspend fun getMusicians(): List<Musician> {
        return service.getMusicians()
    }

    suspend fun getMusician(id: Int): Musician {
        return service.getMusician(id)
    }

    suspend fun addAlbumToMusician(musicianId: Int, albumId: Int) {
        val response = service.addAlbumToMusician(musicianId, albumId)
        if (!response.isSuccessful) {
            throw IllegalStateException("No fue posible asociar el album al musico")
        }
    }
}
