package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.data.repository.CollectorRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CollectorAlbumFormUiState(
    val albums: List<AlbumOption> = emptyList(),
    val selectedAlbum: AlbumOption? = null,
    val price: String = "",
    val status: String = "Active",
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val validationError: String? = null,
    val success: Boolean = false
)

class CollectorAlbumFormViewModel : ViewModel() {
    private val collectorRepository = CollectorRepository()
    private val albumRepository = AlbumRepository()

    private val _state = MutableStateFlow(CollectorAlbumFormUiState())
    val state: StateFlow<CollectorAlbumFormUiState> = _state

    fun loadAlbums(collectorId: Int) {
        if (_state.value.albums.isNotEmpty() || _state.value.loading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val allAlbumsDeferred = async { albumRepository.getAlbums(forceRefresh = true) }
                val collectorDeferred = async { collectorRepository.getCollector(collectorId, forceRefresh = true) }

                val collectorAlbumIds = collectorDeferred.await().collectorAlbums.map { it.id }.toSet()
                val availableAlbums = allAlbumsDeferred.await()
                    .filterNot { it.id in collectorAlbumIds }
                    .map { AlbumOption(id = it.id, name = it.name) }
                    .sortedBy { it.name.lowercase() }

                _state.value = _state.value.copy(
                    albums = availableAlbums,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Error al cargar álbumes"
                )
            }
        }
    }

    fun selectAlbum(album: AlbumOption) {
        _state.value = _state.value.copy(
            selectedAlbum = album,
            validationError = null,
            error = null
        )
    }

    fun setPrice(price: String) {
        _state.value = _state.value.copy(price = price, validationError = null)
    }

    fun setStatus(status: String) {
        _state.value = _state.value.copy(status = status)
    }

    fun saveAlbum(collectorId: Int) {
        val selectedAlbum = _state.value.selectedAlbum
        if (selectedAlbum == null) {
            _state.value = _state.value.copy(validationError = "Debes seleccionar un álbum")
            return
        }
        val priceValue = _state.value.price.toDoubleOrNull()
        if (priceValue == null || priceValue < 0) {
            _state.value = _state.value.copy(validationError = "Ingresa un precio válido (número mayor o igual a 0)")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(saving = true, error = null, validationError = null)
            try {
                collectorRepository.addAlbumToCollector(
                    collectorId = collectorId,
                    albumId = selectedAlbum.id,
                    price = priceValue,
                    status = _state.value.status
                )
                _state.value = _state.value.copy(saving = false, success = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    saving = false,
                    error = e.message ?: "Error al guardar el álbum"
                )
            }
        }
    }
}
