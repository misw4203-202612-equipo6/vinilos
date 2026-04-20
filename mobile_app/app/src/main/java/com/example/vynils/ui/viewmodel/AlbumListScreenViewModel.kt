package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Album
import kotlinx.coroutines.launch
import com.example.vynils.utils.DateUtils

data class AlbumUiState(
    val allAlbums: List<Album> = emptyList(),
    val filteredAlbums: List<Album> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val filterName: String = "",
    val filterGenre: String = "",
    val filterYear: String = ""
)

class AlbumViewModel : ViewModel() {
    private val _state = MutableStateFlow(AlbumUiState())
    val state: StateFlow<AlbumUiState> = _state
    private val repository = AlbumRepository()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val albums = repository.getAlbums()
                _state.value = _state.value.copy(
                    allAlbums = albums,
                    filteredAlbums = albums,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Error al cargar álbumes: ${e.message}", loading = false)
            }
        }
    }

    fun updateFilters(name: String, genre: String, year: String) {
        val filtered = _state.value.allAlbums.filter { album ->
            val matchesName = album.name.contains(name, ignoreCase = true)
            val matchesGenre = genre.isEmpty() || album.genre.contains(genre, ignoreCase = true)
            val matchesYear = year.isEmpty() || album.releaseDate.contains(year)
            
            matchesName && matchesGenre && matchesYear
        }
        
        _state.value = _state.value.copy(
            filterName = name,
            filterGenre = genre,
            filterYear = year,
            filteredAlbums = filtered
        )
    }
    
    fun clearFilters() {
        _state.value = _state.value.copy(
            filterName = "",
            filterGenre = "",
            filterYear = "",
            filteredAlbums = _state.value.allAlbums
        )
    }
}