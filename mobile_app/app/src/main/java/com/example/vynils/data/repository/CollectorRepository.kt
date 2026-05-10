package com.example.vynils.data.repository

import com.example.vynils.data.remote.CollectorService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Collector

class CollectorRepository(
    private val service: CollectorService = RetrofitInstance.collectorService
) {
    suspend fun getCollectors(): List<Collector> {
        cachedCollectors?.let { return it }

        return service.getCollectors().also { collectors ->
            cachedCollectors = collectors
        }
    }

    suspend fun getCollector(id: Int): Collector {
        cachedCollectorDetails[id]?.let { return it }

        return service.getCollector(id).also { collector ->
            cachedCollectorDetails[id] = collector
        }
    }

    companion object {
        private var cachedCollectors: List<Collector>? = null
        private val cachedCollectorDetails = mutableMapOf<Int, Collector>()
    }
}
