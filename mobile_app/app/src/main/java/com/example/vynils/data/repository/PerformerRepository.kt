package com.example.vynils.data.repository

import com.example.vynils.data.remote.PerformerService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Performer

class PerformerRepository(
    private val service: PerformerService = RetrofitInstance.performerService
) {
    suspend fun getPerformers(): List<Performer> {
        cachedPerformers?.let { return it }

        return service.getPerformers().also { performers ->
            cachedPerformers = performers
        }
    }

    companion object {
        private var cachedPerformers: List<Performer>? = null

        fun invalidatePerformers() {
            cachedPerformers = null
        }
    }
}
