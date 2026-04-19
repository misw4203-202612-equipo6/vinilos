package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Collector
import com.example.vynils.data.repository.CollectorRepository
import kotlinx.coroutines.launch

data class CollectorListScreenUiState(
    val allCollectors: List<Collector> = emptyList(),
    val filteredCollectors: List<Collector> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val filterName: String = ""
)

class CollectorListScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(CollectorListScreenUiState())
    val state: StateFlow<CollectorListScreenUiState> = _state
    private val repository = CollectorRepository()

    fun loadCollectors() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val collectors = repository.getCollectors()
                _state.value = _state.value.copy(
                    allCollectors = collectors,
                    filteredCollectors = collectors,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Error al cargar coleccionistas: ${e.message}", loading = false)
            }
        }
    }

    fun updateFilter(name: String) {
        val filtered = _state.value.allCollectors.filter { collector ->
            collector.name.contains(name, ignoreCase = true)
        }
        _state.value = _state.value.copy(
            filterName = name,
            filteredCollectors = filtered
        )
    }

    fun clearFilters() {
        _state.value = _state.value.copy(
            filterName = "",
            filteredCollectors = _state.value.allCollectors
        )
    }
}