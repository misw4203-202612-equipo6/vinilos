package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Artist
import kotlinx.coroutines.launch
import com.example.vynils.data.repository.ArtistRepository

data class ArtistDetailScreenUiState(
    val artist: Artist? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class ArtistDetailScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(ArtistDetailScreenUiState())
    val state: StateFlow<ArtistDetailScreenUiState> = _state
    private val repository = ArtistRepository()

    fun loadArtist(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val artist = repository.getArtist(id)
                _state.value = ArtistDetailScreenUiState(artist = artist, loading = false)
            } catch (e: Exception) {
                _state.value = ArtistDetailScreenUiState(error = "Error al cargar artista: ${e.message}", loading = false)
            }
        }
    }
}