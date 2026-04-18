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
}
