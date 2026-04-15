package com.example.vynils.data.repository

import com.example.vynils.data.remote.AlbumService
import com.example.vynils.data.remote.RetrofitInstance
import com.example.vynils.model.Album

class AlbumRepository(
    private val service: AlbumService = RetrofitInstance.albumService
) {
    suspend fun getAlbums(): List<Album> {
        return service.getAlbums()
    }

    suspend fun getAlbum(id: Int): Album {
        return service.getAlbum(id)
    }

    suspend fun getAlbumsMock(): List<Album>{
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
        return fakeAlbums;
    }

    suspend fun getAlbumMock(id: Int): Album{
        return Album(1, "Thriller", "", "1982-11-30", "Michael Jackson album", "Pop", "Sony");
    }
}