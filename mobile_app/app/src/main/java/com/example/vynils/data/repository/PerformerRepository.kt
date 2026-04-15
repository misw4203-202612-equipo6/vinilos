package com.example.vynils.data.repository

import com.example.vynils.data.remote.PerformerService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Performer

class PerformerRepository(
    private val service: PerformerService = RetrofitInstance.performerService
) {
    suspend fun getPerformers(): List<Performer> {
        return service.getPerformers()
    }

    suspend fun getPerformer(id: Int): Performer {
        return service.getPerformer(id)
    }
}