package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.CollectorRepository
import com.example.vynils.model.Collector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CollectorDetailUiState(
    val collector: Collector? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class CollectorDetailScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(CollectorDetailUiState())
    val state: StateFlow<CollectorDetailUiState> = _state
    private val repository = CollectorRepository()

    fun loadCollector(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val collector = repository.getCollector(id)
                _state.value = CollectorDetailUiState(collector = collector, loading = false)
            } catch (e: Exception) {
                _state.value = CollectorDetailUiState(error = "Error al cargar coleccionista: ${e.message}", loading = false)
            }
        }
    }
}