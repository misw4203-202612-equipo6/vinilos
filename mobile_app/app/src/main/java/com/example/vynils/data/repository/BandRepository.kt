package com.example.vynils.data.repository

import com.example.vynils.data.remote.BandService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Band

class BandRepository(
    private val service: BandService = RetrofitInstance.bandService
) {
    suspend fun getBands(forceRefresh: Boolean = false): List<Band> {
        if (!forceRefresh) {
            cachedBands?.let { return it }
        }

        return service.getBands().also { bands ->
            cachedBands = bands
        }
    }

    suspend fun getBand(id: Int, forceRefresh: Boolean = false): Band {
        if (!forceRefresh) {
            cachedBandDetails[id]?.let { return it }
        }

        return service.getBand(id).also { band ->
            cachedBandDetails[id] = band
        }
    }

    suspend fun addAlbumToBand(bandId: Int, albumId: Int) {
        val response = service.addAlbumToBand(bandId, albumId)
        if (!response.isSuccessful) {
            throw IllegalStateException("No fue posible asociar el album a la banda")
        }
        invalidateBand(bandId)
        AlbumRepository.invalidateAlbum(albumId)
        PerformerRepository.invalidatePerformers()
    }

    companion object {
        private var cachedBands: List<Band>? = null
        private val cachedBandDetails = mutableMapOf<Int, Band>()

        fun invalidateBands() {
            cachedBands = null
            cachedBandDetails.clear()
        }

        fun invalidateBand(id: Int) {
            cachedBands = null
            cachedBandDetails.remove(id)
        }
    }
}
