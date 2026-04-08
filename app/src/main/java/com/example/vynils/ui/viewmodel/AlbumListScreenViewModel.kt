package com.example.vynils.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynils.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.vynils.model.Album
import kotlinx.coroutines.launch

data class AlbumUiState(
    val albums: List<Album> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class AlbumViewModel : ViewModel() {
    private val _state = MutableStateFlow(AlbumUiState())
    val state: StateFlow<AlbumUiState> = _state

    private val repository = AlbumRepository()

    fun loadAlbumsMock() {
        val fakeAlbums = listOf(
            Album(1, "Thriller", "", "1982-11-30", "Michael Jackson album", "Pop", "Sony"),
            Album(2, "Back in Black", "", "1980-07-25", "AC/DC album", "Rock", "Universal"),
            Album(3, "The Dark Side of the Moon", "", "1973-03-01", "Pink Floyd album", "Rock", "EMI"),
            Album(4, "Rumours", "", "1977-02-04", "Fleetwood Mac album", "Rock", "Warner"),
            Album(5, "21", "", "2011-01-24", "Adele album", "Pop", "XL"),
            Album(6, "Abbey Road", "", "1969-09-26", "The Beatles album", "Rock", "Apple"),
            Album(7, "Hotel California", "", "1976-12-08", "Eagles album", "Rock", "Asylum"),
            Album(8, "Born in the U.S.A.", "", "1984-06-04", "Bruce Springsteen album", "Rock", "Columbia"),
            Album(9, "1989", "", "2014-10-27", "Taylor Swift album", "Pop", "Big Machine"),
            Album(10, "Nevermind", "", "1991-09-24", "Nirvana album", "Rock", "DGC"),
            Album(10, "Nevermind", "", "1991-09-24", "Nirvana album", "Rock", "DGC"),
            Album(10, "Nevermind", "", "1991-09-24", "Nirvana album", "Rock", "DGC"),
            Album(10, "Nevermind", "", "1991-09-24", "Nirvana album", "Rock", "DGC"),
            Album(10, "Nevermind", "", "1991-09-24", "Nirvana album", "Rock", "DGC")

        )

        _state.value = AlbumUiState(albums = fakeAlbums)
    }

    fun loadAlbums() {
        viewModelScope.launch {
            try {
                val albums = repository.getAlbums()
                _state.value = AlbumUiState(albums = albums)
            } catch (e: Exception) {
                _state.value = AlbumUiState(error = e.message)
            }
        }
    }

}