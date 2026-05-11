package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.data.repository.BandRepository
import com.example.vynils.data.repository.MusicianRepository
import com.example.vynils.model.PerformerItem
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class ArtistType {
    BAND,
    MUSICIAN
}

data class ArtistOption(
    val id: Int,
    val name: String,
    val type: ArtistType
)

data class AlbumArtistFormUiState(
    val artists: List<ArtistOption> = emptyList(),
    val selectedArtist: ArtistOption? = null,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val validationError: String? = null,
    val success: Boolean = false
)

class AlbumArtistFormViewModel : ViewModel() {
    private val albumRepository = AlbumRepository()
    private val bandRepository = BandRepository()
    private val musicianRepository = MusicianRepository()

    private val _state = MutableStateFlow(AlbumArtistFormUiState())
    val state: StateFlow<AlbumArtistFormUiState> = _state

    fun loadArtists(albumId: Int) {
        if (_state.value.artists.isNotEmpty() || _state.value.loading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val albumDeferred = async { albumRepository.getAlbum(albumId, forceRefresh = true) }
                val bandsDeferred = async { bandRepository.getBands(forceRefresh = true) }
                val musiciansDeferred = async { musicianRepository.getMusicians(forceRefresh = true) }
                val assignedPerformers = albumDeferred.await().performers.orEmpty()

                val artists = bandsDeferred.await().map {
                    ArtistOption(id = it.id, name = it.name, type = ArtistType.BAND)
                } + musiciansDeferred.await().map {
                    ArtistOption(id = it.id, name = it.name, type = ArtistType.MUSICIAN)
                }

                val availableArtists = artists.filterNot { artist ->
                    assignedPerformers.any { performer ->
                        performer.id == artist.id && performer.toArtistType() == artist.type
                    }
                }

                _state.value = _state.value.copy(
                    artists = availableArtists.sortedBy { it.name.lowercase() },
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

    fun saveArtist(albumId: Int) {
        val selectedArtist = _state.value.selectedArtist
        if (selectedArtist == null) {
            _state.value = _state.value.copy(validationError = "Debes seleccionar un artista")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(saving = true, error = null, validationError = null)
            try {
                when (selectedArtist.type) {
                    ArtistType.BAND -> bandRepository.addAlbumToBand(selectedArtist.id, albumId)
                    ArtistType.MUSICIAN -> musicianRepository.addAlbumToMusician(selectedArtist.id, albumId)
                }
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

private fun PerformerItem.toArtistType(): ArtistType {
    return if (creationDate != null) ArtistType.BAND else ArtistType.MUSICIAN
}
