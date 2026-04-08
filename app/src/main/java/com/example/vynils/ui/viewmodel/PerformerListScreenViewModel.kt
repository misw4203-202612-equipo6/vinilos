package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Performer

data class PerformerListScreenUiState(
    val collectors: List<Performer> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class PerformerListScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(PerformerListScreenUiState())
    val state: StateFlow<PerformerListScreenUiState> = _state
    fun loadPerformers() {
        val fakePerformers = listOf(
            Performer(
                id = 1,
                name = "Michael Jackson",
                image = "",
                description = "King of Pop",
                collectors = emptyList(),
                albums = emptyList()
            ),
            Performer(
                id = 2,
                name = "Freddie Mercury",
                image = "",
                description = "Lead singer of Queen",
                collectors = emptyList(),
                albums = emptyList(),
            ),
            Performer(
                id = 3,
                name = "Madonna",
                image = "",
                description = "Queen of Pop",
                collectors = emptyList(),
                albums = emptyList(),
            ),
            Performer(
                id = 4,
                name = "Elvis Presley",
                image = "",
                description = "King of Rock and Roll",
                collectors = emptyList(),
                albums = emptyList(),
            ),
            Performer(
                id = 5,
                name = "Beyoncé",
                image = "",
                description = "Global pop icon",
                collectors = emptyList(),
                albums = emptyList(),
            )
        )
        _state.value = PerformerListScreenUiState(collectors = fakePerformers)
    }
}