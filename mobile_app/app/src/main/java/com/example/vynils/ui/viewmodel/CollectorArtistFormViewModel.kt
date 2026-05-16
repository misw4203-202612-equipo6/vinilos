package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.BandRepository
import com.example.vynils.data.repository.CollectorRepository
import com.example.vynils.data.repository.MusicianRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CollectorArtistFormUiState(
    val artists: List<ArtistOption> = emptyList(),
    val selectedArtist: ArtistOption? = null,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val validationError: String? = null,
    val success: Boolean = false
)

class CollectorArtistFormViewModel : ViewModel() {
    private val collectorRepository = CollectorRepository()
    private val bandRepository = BandRepository()
    private val musicianRepository = MusicianRepository()

    private val _state = MutableStateFlow(CollectorArtistFormUiState())
    val state: StateFlow<CollectorArtistFormUiState> = _state

    fun loadArtists(collectorId: Int) {
        if (_state.value.artists.isNotEmpty() || _state.value.loading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val favoritesDeferred = async { collectorRepository.getPerformersByCollector(collectorId, forceRefresh = true) }
                val bandsDeferred = async { bandRepository.getBands(forceRefresh = true) }
                val musiciansDeferred = async { musicianRepository.getMusicians(forceRefresh = true) }

                val favoriteIds = favoritesDeferred.await().map { it.id }.toSet()

                val artists = bandsDeferred.await().map {
                    ArtistOption(id = it.id, name = it.name, type = ArtistType.BAND)
                } + musiciansDeferred.await().map {
                    ArtistOption(id = it.id, name = it.name, type = ArtistType.MUSICIAN)
                }

                val availableArtists = artists
                    .filterNot { it.id in favoriteIds }
                    .sortedBy { it.name.lowercase() }

                _state.value = _state.value.copy(
                    artists = availableArtists,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Error al cargar artistas"
                )
            }
        }
    }

    fun selectArtist(artist: ArtistOption) {
        _state.value = _state.value.copy(
            selectedArtist = artist,
            validationError = null,
            error = null
        )
    }

    fun saveArtist(collectorId: Int) {
        val selectedArtist = _state.value.selectedArtist
        if (selectedArtist == null) {
            _state.value = _state.value.copy(validationError = "Debes seleccionar un artista")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(saving = true, error = null, validationError = null)
            try {
                collectorRepository.addArtistToCollector(
                    collectorId = collectorId,
                    artistId = selectedArtist.id,
                    artistType = selectedArtist.type.name.lowercase()
                )
                _state.value = _state.value.copy(saving = false, success = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    saving = false,
                    error = e.message ?: "Error al guardar el artista"
                )
            }
        }
    }
}
