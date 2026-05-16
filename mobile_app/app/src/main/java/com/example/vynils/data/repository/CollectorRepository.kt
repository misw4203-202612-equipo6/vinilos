package com.example.vynils.data.repository

import com.example.vynils.data.remote.CollectorService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Collector
import com.example.vynils.model.CollectorAlbumRequest
import com.example.vynils.model.PerformerItem

class CollectorRepository(
    private val service: CollectorService = RetrofitInstance.collectorService
) {
    suspend fun getCollectors(): List<Collector> {
        cachedCollectors?.let { return it }

        return service.getCollectors().also { collectors ->
            cachedCollectors = collectors
        }
    }

    suspend fun getCollector(id: Int, forceRefresh: Boolean = false): Collector {
        if (!forceRefresh) {
            cachedCollectorDetails[id]?.let { return it }
        }

        return service.getCollector(id).also { collector ->
            cachedCollectorDetails[id] = collector
        }
    }

    suspend fun getPerformersByCollector(collectorId: Int, forceRefresh: Boolean = false): List<PerformerItem> {
        if (!forceRefresh) {
            cachedPerformersByCollector[collectorId]?.let { return it }
        }

        return service.getPerformersByCollector(collectorId).also { performers ->
            cachedPerformersByCollector[collectorId] = performers
        }
    }

    suspend fun addArtistToCollector(collectorId: Int, artistId: Int, artistType: String) {
        val response = when (artistType.lowercase()) {
            "band" -> service.addBandToCollector(collectorId, artistId)
            "musician" -> service.addMusicianToCollector(collectorId, artistId)
            else -> throw IllegalArgumentException("Tipo de artista inválido: $artistType")
        }
        if (!response.isSuccessful) {
            throw IllegalStateException("No fue posible agregar el artista como favorito")
        }
        invalidateCollector(collectorId)
    }

    suspend fun addAlbumToCollector(collectorId: Int, albumId: Int, price: Double, status: String) {
        val response = service.addAlbumToCollector(
            collectorId = collectorId,
            albumId = albumId,
            request = CollectorAlbumRequest(price = price, status = status)
        )
        if (!response.isSuccessful) {
            throw IllegalStateException("No fue posible agregar el álbum a la colección")
        }
        invalidateCollector(collectorId)
    }

    companion object {
        private var cachedCollectors: List<Collector>? = null
        private val cachedCollectorDetails = mutableMapOf<Int, Collector>()
        private val cachedPerformersByCollector = mutableMapOf<Int, List<PerformerItem>>()

        fun invalidateCollector(id: Int) {
            cachedCollectors = null
            cachedCollectorDetails.remove(id)
            cachedPerformersByCollector.remove(id)
        }
    }
}
