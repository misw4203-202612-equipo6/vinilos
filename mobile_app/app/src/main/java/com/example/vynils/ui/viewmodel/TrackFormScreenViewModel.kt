package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.model.CreateTrackRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TrackDraft(
    val localId: Int,
    val name: String,
    val duration: String
)

data class TrackFormUiState(
    val name: String = "",
    val duration: String = "",
    val tracks: List<TrackDraft> = emptyList(),
    val editingTrackId: Int? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val validationError: String? = null,
    val success: Boolean = false,
    val successMessage: String? = null
)

class TrackFormScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrackFormUiState())
    val state: StateFlow<TrackFormUiState> = _state
    private val repository = AlbumRepository()
    private var nextLocalId = 1

    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name, validationError = null, error = null)
    }

    fun updateDuration(duration: String) {
        _state.value = _state.value.copy(duration = duration, validationError = null, error = null)
    }

    fun addOrUpdateTrack() {
        val name = _state.value.name.trim()
        val duration = _state.value.duration.trim()

        if (name.isBlank() || duration.isBlank()) {
            _state.value = _state.value.copy(validationError = "Nombre y duración son obligatorios")
            return
        }

        val updatedTracks = _state.value.editingTrackId?.let { editingId ->
            _state.value.tracks.map { track ->
                if (track.localId == editingId) track.copy(name = name, duration = duration) else track
            }
        } ?: (_state.value.tracks + TrackDraft(localId = nextLocalId++, name = name, duration = duration))

        _state.value = _state.value.copy(
            name = "",
            duration = "",
            tracks = updatedTracks,
            editingTrackId = null,
            validationError = null,
            error = null
        )
    }

    fun editTrack(localId: Int) {
        val track = _state.value.tracks.firstOrNull { it.localId == localId } ?: return
        _state.value = _state.value.copy(
            name = track.name,
            duration = track.duration,
            editingTrackId = localId,
            validationError = null,
            error = null
        )
    }

    fun deleteTrack(localId: Int) {
        _state.value = _state.value.copy(
            tracks = _state.value.tracks.filterNot { it.localId == localId },
            editingTrackId = if (_state.value.editingTrackId == localId) null else _state.value.editingTrackId,
            name = if (_state.value.editingTrackId == localId) "" else _state.value.name,
            duration = if (_state.value.editingTrackId == localId) "" else _state.value.duration
        )
    }

    fun saveTracks(albumId: Int) {
        if (_state.value.tracks.isEmpty()) {
            _state.value = _state.value.copy(validationError = "Debes agregar al menos un track")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null, validationError = null)
            try {
                _state.value.tracks.forEach { track ->
                    val trackRequest = CreateTrackRequest(name = track.name, duration = track.duration)
                    repository.addTrackToAlbum(albumId, trackRequest)
                }
                _state.value = _state.value.copy(
                    loading = false,
                    success = true,
                    successMessage = "Tracks asociados exitosamente",
                    tracks = emptyList(),
                    name = "",
                    duration = "",
                    editingTrackId = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, loading = false)
            }
        }
    }

    fun clearState() {
        _state.value = TrackFormUiState()
    }
}
