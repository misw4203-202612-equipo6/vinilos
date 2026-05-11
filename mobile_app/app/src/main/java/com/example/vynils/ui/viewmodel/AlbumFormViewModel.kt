package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import com.example.vynils.data.repository.BandRepository
import com.example.vynils.data.repository.MusicianRepository
import com.example.vynils.model.AlbumRequest
import java.time.Year
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class AlbumArtistOption(
    val id: Int,
    val name: String,
    val type: ArtistType
)

data class AlbumFormState(
    val loading: Boolean = false,
    val loadingArtists: Boolean = false,
    val success: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null,
    val artists: List<AlbumArtistOption> = emptyList()
)

class AlbumFormViewModel(
    private val albumRepository: AlbumRepository = AlbumRepository(),
    private val bandRepository: BandRepository = BandRepository(),
    private val musicianRepository: MusicianRepository = MusicianRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumFormState())
    val state: StateFlow<AlbumFormState> = _state.asStateFlow()

    init {
        loadArtists()
    }

    fun loadArtists() {
        if (_state.value.loadingArtists || _state.value.artists.isNotEmpty()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loadingArtists = true, error = null)
            try {
                val bandsDeferred = async { bandRepository.getBands() }
                val musiciansDeferred = async { musicianRepository.getMusicians() }

                val artists = bandsDeferred.await().map {
                    AlbumArtistOption(id = it.id, name = it.name, type = ArtistType.BAND)
                } + musiciansDeferred.await().map {
                    AlbumArtistOption(id = it.id, name = it.name, type = ArtistType.MUSICIAN)
                }

                _state.value = _state.value.copy(
                    loadingArtists = false,
                    artists = artists.sortedBy { it.name.lowercase() }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loadingArtists = false,
                    error = e.message ?: "Error al cargar artistas"
                )
            }
        }
    }

    fun createAlbum(
        name: String,
        selectedArtist: AlbumArtistOption,
        year: String?,
        genre: String?,
        cover: String?,
        releaseDate: String?,
        description: String?,
        recordLabel: String?
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                loading = true,
                success = false,
                successMessage = null,
                error = null
            )
            try {
                val trimmedName = name.trim()
                val releaseYear = year?.trim().takeUnless { it.isNullOrBlank() } ?: Year.now().value.toString()
                val normalizedGenre = genre?.trim().takeUnless { it.isNullOrBlank() } ?: "Rock"
                val normalizedCover = cover?.trim().takeUnless { it.isNullOrBlank() } ?: DEFAULT_COVER_URL
                val normalizedDescription = description?.trim().takeUnless { it.isNullOrBlank() } ?: DEFAULT_DESCRIPTION
                val normalizedRecordLabel = recordLabel?.trim().takeUnless { it.isNullOrBlank() } ?: DEFAULT_RECORD_LABEL
                val normalizedReleaseDate = releaseDate?.trim().takeUnless { it.isNullOrBlank() }
                    ?.let { "${it}T00:00:00-05:00" }
                    ?: "${releaseYear}-01-01T00:00:00-05:00"

                val request = AlbumRequest(
                    name = trimmedName,
                    cover = normalizedCover,
                    releaseDate = normalizedReleaseDate,
                    description = normalizedDescription,
                    genre = normalizedGenre,
                    recordLabel = normalizedRecordLabel
                )
                val createdAlbum = albumRepository.createAlbum(request)

                when (selectedArtist.type) {
                    ArtistType.BAND -> bandRepository.addAlbumToBand(selectedArtist.id, createdAlbum.id)
                    ArtistType.MUSICIAN -> musicianRepository.addAlbumToMusician(selectedArtist.id, createdAlbum.id)
                }

                _state.value = _state.value.copy(
                    loading = false,
                    success = true,
                    successMessage = "Álbum creado exitosamente"
                )
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _state.value = _state.value.copy(
                    loading = false,
                    error = errorBody ?: e.message()
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "No fue posible crear el álbum"
                )
            }
        }
    }

    fun resetState() {
        _state.value = _state.value.copy(success = false, successMessage = null, error = null)
    }

    private companion object {
        const val DEFAULT_COVER_URL = "https://placehold.co/600x600/png"
        const val DEFAULT_DESCRIPTION = "Álbum creado desde la aplicación móvil"
        const val DEFAULT_RECORD_LABEL = "EMI"
    }
}
