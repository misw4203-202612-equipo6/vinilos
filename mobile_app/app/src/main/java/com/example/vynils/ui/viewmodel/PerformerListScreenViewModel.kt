package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.PerformerRepository
import com.example.vynils.model.Performer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PerformerListScreenUiState(
    val allPerformers: List<Performer> = emptyList(),
    val filteredPerformers: List<Performer> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val filterName: String = ""
)

class PerformerListScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(PerformerListScreenUiState())
    val state: StateFlow<PerformerListScreenUiState> = _state
    private val repository = PerformerRepository()

    init {
        loadPerformers()
    }

    fun loadPerformers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val performers = repository.getPerformers()
                _state.value = _state.value.copy(
                    allPerformers = performers,
                    filteredPerformers = performers,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al cargar artistas: ${e.message}",
                    loading = false
                )
            }
        }
    }

    fun updateFilter(name: String) {
        val filtered = _state.value.allPerformers.filter { performer ->
            performer.name.contains(name, ignoreCase = true)
        }
        _state.value = _state.value.copy(
            filterName = name,
            filteredPerformers = filtered
        )
    }

    fun clearFilters() {
        _state.value = _state.value.copy(
            filterName = "",
            filteredPerformers = _state.value.allPerformers
        )
    }
}
