package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Artist
import kotlinx.coroutines.launch
import com.example.vynils.data.repository.ArtistRepository

data class ArtistListScreenUiState(
    val allArtists: List<Artist> = emptyList(),
    val filteredArtists: List<Artist> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val filterName: String = ""
)

class ArtistListScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(ArtistListScreenUiState())
    val state: StateFlow<ArtistListScreenUiState> = _state
    private val repository = ArtistRepository()

    init {
        loadArtists()
    }

    fun loadArtists() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val artists = repository.getArtists()
                _state.value = _state.value.copy(
                    allArtists = artists,
                    filteredArtists = artists,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Error al cargar artistas: ${e.message}", loading = false)
            }
        }
    }

    fun updateFilter(name: String) {
        val filtered = _state.value.allArtists.filter { artist ->
            artist.name.contains(name, ignoreCase = true)
        }
        _state.value = _state.value.copy(
            filterName = name,
            filteredArtists = filtered
        )
    }

    fun clearFilters() {
        _state.value = _state.value.copy(
            filterName = "",
            filteredArtists = _state.value.allArtists
        )
    }

    fun loadArtistsMock() {
        val fakeArtists = listOf(
            Artist(
                id = 1,
                name = "Michael Jackson",
                image = "",
                description = "King of Pop",
                collectors = emptyList(),
                albums = emptyList(),
                musicians =  emptyList(),
            ),
            Artist(
                id = 2,
                name = "Freddie Mercury",
                image = "",
                description = "Lead singer of Queen",
                collectors = emptyList(),
                albums = emptyList(),
                musicians =  emptyList(),
            ),
            Artist(
                id = 3,
                name = "Madonna",
                image = "",
                description = "Queen of Pop",
                collectors = emptyList(),
                albums = emptyList(),
                musicians =  emptyList(),
            ),
            Artist(
                id = 4,
                name = "Elvis Presley",
                image = "",
                description = "King of Rock and Roll",
                collectors = emptyList(),
                albums = emptyList(),
                musicians =  emptyList(),
            ),
            Artist(
                id = 5,
                name = "Beyoncé",
                image = "",
                description = "Global pop icon",
                collectors = emptyList(),
                albums = emptyList(),
                musicians =  emptyList(),
            )
        )
        _state.value = _state.value.copy(allArtists = fakeArtists, filteredArtists = fakeArtists)
    }
}