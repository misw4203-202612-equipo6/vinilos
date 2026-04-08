package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Collector

data class CollectorListScreenUiState(
    val collectors: List<Collector> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class CollectorListScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(CollectorListScreenUiState())
    val state: StateFlow<CollectorListScreenUiState> = _state
    fun loadCollectors() {
        val fakeCollectors = listOf(
            Collector(
                id = 1,
                name = "Juan Pérez",
                telephone = "3001234567",
                email = "juan@example.com",
                favoritePerformers = emptyList(),
                comments = emptyList(),
                collectorAlbums = emptyList()
            ),
            Collector(
                id = 2,
                name = "María Gómez",
                telephone = "3012345678",
                email = "maria@example.com",
                favoritePerformers = emptyList(),
                comments = emptyList(),
                collectorAlbums = emptyList()
            ),
            Collector(
                id = 3,
                name = "Carlos López",
                telephone = "3023456789",
                email = "carlos@example.com",
                favoritePerformers = emptyList(),
                comments = emptyList(),
                collectorAlbums = emptyList()
            ),
            Collector(
                id = 4,
                name = "Ana Rodríguez",
                telephone = "3034567890",
                email = "ana@example.com",
                favoritePerformers = emptyList(),
                comments = emptyList(),
                collectorAlbums = emptyList()
            ),
            Collector(
                id = 5,
                name = "Luis Martínez",
                telephone = "3045678901",
                email = "luis@example.com",
                favoritePerformers = emptyList(),
                comments = emptyList(),
                collectorAlbums = emptyList()
            )
        )
        _state.value = CollectorListScreenUiState(collectors = fakeCollectors)
    }
}