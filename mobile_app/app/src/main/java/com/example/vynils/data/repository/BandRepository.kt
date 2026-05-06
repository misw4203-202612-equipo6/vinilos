package com.example.vynils.data.repository

import com.example.vynils.data.remote.BandService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Band

class BandRepository(
    private val service: BandService = RetrofitInstance.bandService
) {
    suspend fun getBands(): List<Band> {
        return service.getBands()
    }

    suspend fun getBand(id: Int): Band {
        return service.getBand(id)
    }

    suspend fun addAlbumToBand(bandId: Int, albumId: Int) {
        val response = service.addAlbumToBand(bandId, albumId)
        if (!response.isSuccessful) {
            throw IllegalStateException("No fue posible asociar el album a la banda")
        }
    }
}
