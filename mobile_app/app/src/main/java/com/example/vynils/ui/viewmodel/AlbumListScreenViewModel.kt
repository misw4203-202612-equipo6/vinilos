package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Album
import kotlinx.coroutines.launch

data class AlbumUiState(
    val albums: List<Album> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class AlbumViewModel : ViewModel() {
    private val _state = MutableStateFlow(AlbumUiState())
    val state: StateFlow<AlbumUiState> = _state
    private val repository = AlbumRepository()
    fun loadAlbums() {
        viewModelScope.launch {
            try {
                val albums = repository.getAlbums()
                _state.value = AlbumUiState(albums = albums)
            } catch (e: Exception) {
                _state.value = AlbumUiState(error = e.message)
            }
        }
    }
}