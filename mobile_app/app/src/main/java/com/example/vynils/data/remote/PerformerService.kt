package com.example.vynils.data.remote

import com.example.vynils.model.Performer
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PerformerService(
    private val bandService: BandService,
    private val musicianService: MusicianService
) {
    suspend fun getPerformers(): List<Performer> = coroutineScope {
        val bands = async { bandService.getBands() }
        val musicians = async { musicianService.getMusicians() }

        bands.await() + musicians.await()
    }
}
