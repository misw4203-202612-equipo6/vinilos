package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.model.AlbumRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class AlbumFormState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AlbumFormViewModel(
    private val repository: AlbumRepository = AlbumRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumFormState())
    val state: StateFlow<AlbumFormState> = _state.asStateFlow()

    fun createAlbum(name: String, cover: String, releaseDate: String, description: String, genre: String, recordLabel: String) {
        viewModelScope.launch {
            _state.value = AlbumFormState(loading = true)
            try {
                // Trim all inputs to avoid validation errors due to accidental spaces
                val trimmedName = name.trim()
                val trimmedCover = cover.trim()
                val trimmedReleaseDate = releaseDate.trim()
                val trimmedDescription = description.trim()
                val trimmedGenre = genre.trim()
                val trimmedRecordLabel = recordLabel.trim()

                // Formatting date to ISO 8601 as requested: YYYY-MM-DDT00:00:00-05:00
                val formattedDate = "${trimmedReleaseDate}T00:00:00-05:00"
                
                val request = AlbumRequest(
                    name = trimmedName,
                    cover = trimmedCover,
                    releaseDate = formattedDate,
                    description = trimmedDescription,
                    genre = trimmedGenre,
                    recordLabel = trimmedRecordLabel
                )
                repository.createAlbum(request)
                _state.value = AlbumFormState(success = true)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _state.value = AlbumFormState(error = errorBody ?: e.message())
            } catch (e: Exception) {
                _state.value = AlbumFormState(error = e.message)
            }
        }
    }

    fun resetState() {
        _state.value = AlbumFormState()
    }
}