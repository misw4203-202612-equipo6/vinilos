package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.CreateTrackRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TrackFormUiState(
    val name: String = "",
    val duration: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class TrackFormScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrackFormUiState())
    val state: StateFlow<TrackFormUiState> = _state

    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun updateDuration(duration: String) {
        _state.value = _state.value.copy(duration = duration)
    }

    fun createTrack(albumId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val trackRequest = CreateTrackRequest(name = _state.value.name, duration = _state.value.duration)
                RetrofitInstance.albumService.addTrackToAlbum(albumId, trackRequest)
                _state.value = _state.value.copy(loading = false, success = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, loading = false)
            }
        }
    }
}
