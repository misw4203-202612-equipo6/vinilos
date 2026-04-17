package com.example.vynils.data.remote

import com.example.vynils.model.Performer

class PerformerService(
    private val bandService: BandService,
    private val musicianService: MusicianService
) {
    suspend fun getPerformers(): List<Performer> {
        return bandService.getBands() + musicianService.getMusicians()
    }
}
