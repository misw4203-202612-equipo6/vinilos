package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.data.repository.BandRepository
import com.example.vynils.data.repository.MusicianRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AlbumOption(
    val id: Int,
    val name: String
)

data class ArtistAlbumFormUiState(
    val albums: List<AlbumOption> = emptyList(),
    val selectedAlbum: AlbumOption? = null,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val validationError: String? = null,
    val success: Boolean = false
)

class ArtistAlbumFormViewModel : ViewModel() {
    private val albumRepository = AlbumRepository()
    private val bandRepository = BandRepository()
    private val musicianRepository = MusicianRepository()

    private val _state = MutableStateFlow(ArtistAlbumFormUiState())
    val state: StateFlow<ArtistAlbumFormUiState> = _state

    fun loadAlbums(artistId: Int, artistType: String) {
        if (_state.value.albums.isNotEmpty() || _state.value.loading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val albumsDeferred = async { albumRepository.getAlbums(forceRefresh = true) }
                val assignedAlbumsDeferred = async {
                    when (artistType.lowercase()) {
                        "band" -> bandRepository.getBand(artistId, forceRefresh = true).albums
                        "musician" -> musicianRepository.getMusician(artistId, forceRefresh = true).albums
                        else -> throw IllegalArgumentException("Tipo de artista invalido")
                    }
                }

                val assignedAlbumIds = assignedAlbumsDeferred.await().map { it.id }.toSet()
                val availableAlbums = albumsDeferred.await()
                    .filterNot { it.id in assignedAlbumIds }
                    .map { AlbumOption(id = it.id, name = it.name) }
                    .sortedBy { it.name.lowercase() }

                _state.value = _state.value.copy(
                    albums = availableAlbums,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Error al cargar albumes"
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

    fun saveAlbum(artistId: Int, artistType: String) {
        val selectedAlbum = _state.value.selectedAlbum
        if (selectedAlbum == null) {
            _state.value = _state.value.copy(validationError = "Debes seleccionar un album")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(saving = true, error = null, validationError = null)
            try {
                when (artistType.lowercase()) {
                    "band" -> bandRepository.addAlbumToBand(artistId, selectedAlbum.id)
                    "musician" -> musicianRepository.addAlbumToMusician(artistId, selectedAlbum.id)
                    else -> throw IllegalArgumentException("Tipo de artista invalido")
                }
                _state.value = _state.value.copy(saving = false, success = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    saving = false,
                    error = e.message ?: "Error al guardar el album"
                )
            }
        }
    }
}
