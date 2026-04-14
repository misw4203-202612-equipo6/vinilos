package com.example.vynils.data.repository

import com.example.vynils.data.remote.CollectorService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Collector

class CollectorRepository(
    private val service: CollectorService = RetrofitInstance.collectorService
) {
    suspend fun getCollectors(): List<Collector> {
        return service.getCollectors()
    }

    suspend fun getCollector(id: Int): Collector {
        return service.getCollector(id)
    }
}