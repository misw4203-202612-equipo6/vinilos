package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.BandRepository
import com.example.vynils.data.repository.MusicianRepository
import com.example.vynils.model.Band
import com.example.vynils.model.Musician
import com.example.vynils.model.Performer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PerformerDetailScreenUiState(
    val performer: Performer? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class PerformerDetailScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(PerformerDetailScreenUiState())
    val state: StateFlow<PerformerDetailScreenUiState> = _state
    private val bandRepository = BandRepository()
    private val musicianRepository = MusicianRepository()

    fun loadPerformer(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val performer = try {
                    bandRepository.getBand(id)
                } catch (_: Exception) {
                    musicianRepository.getMusician(id)
                }
                _state.value = PerformerDetailScreenUiState(performer = performer, loading = false)
            } catch (e: Exception) {
                _state.value = PerformerDetailScreenUiState(error = "Error al cargar artista: ${e.message}", loading = false)
            }
        }
    }
}
