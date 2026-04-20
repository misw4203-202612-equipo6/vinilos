package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.data.repository.BandRepository
import com.example.vynils.data.repository.MusicianRepository
import com.example.vynils.model.Band
import com.example.vynils.model.Musician
import com.example.vynils.model.Performer
import com.example.vynils.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PerformerDetailScreenUiState(
    val performer: Performer? = null,
    val tracks: List<Track> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class PerformerDetailScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(PerformerDetailScreenUiState())
    val state: StateFlow<PerformerDetailScreenUiState> = _state
    private val bandRepository = BandRepository()
    private val musicianRepository = MusicianRepository()
    private val albumRepository = AlbumRepository()

    fun loadPerformer(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val performer = try {
                    bandRepository.getBand(id)
                } catch (_: Exception) {
                    musicianRepository.getMusician(id)
                }
                val albums = when (performer) {
                    is Band -> performer.albums
                    is Musician -> performer.albums
                    else -> emptyList()
                }
                val tracks = mutableListOf<Track>()
                albums.forEach { album ->
                    val albumTracks = runCatching { albumRepository.getAlbum(album.id).tracks }.getOrDefault(emptyList())
                    tracks.addAll(albumTracks)
                }
                _state.value = PerformerDetailScreenUiState(
                    performer = performer,
                    tracks = tracks,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = PerformerDetailScreenUiState(error = "Error al cargar artista: ${e.message}", loading = false)
            }
        }
    }
}
