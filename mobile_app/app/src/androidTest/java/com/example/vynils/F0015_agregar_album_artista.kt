package com.example.vynils

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class F0015_agregar_album_artista {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val allArtists by lazy { obtenerTodosLosArtistas() }
    private val allAlbums by lazy { obtenerTodosLosAlbumes() }

    @Test
    fun desde_detalle_album_permite_agregar_artista_filtra_asignados_y_guarda() {
        val fixture = prepararEscenarioAlbumConUnArtistaAsignado()

        try {
            abrirDetalleDeAlbum(fixture.albumName)
            abrirFormularioAgregarArtistaDesdeAlbum()
            esperarFormularioAlbumArtistConOpciones()

            composeTestRule.onNodeWithText("Guardar").assertIsNotEnabled()
            composeTestRule.onNodeWithTag("album-artist-dropdown-toggle", useUnmergedTree = true).performClick()
            composeTestRule.onAllNodesWithText(fixture.existingArtist.label).assertCountEquals(0)
            composeTestRule.onNodeWithText(fixture.availableArtist.label).assertIsDisplayed()
            composeTestRule.onNodeWithText(fixture.availableArtist.label).performClick()
            composeTestRule.onNodeWithText("Guardar").performClick()

            esperarDetalleAlbum()
            composeTestRule.onNodeWithText(fixture.availableArtist.name, substring = true).assertIsDisplayed()
        } finally {
            limpiarAsociacionesAlbum(fixture.albumId)
        }
    }

    @Test
    fun desde_detalle_album_muestra_advertencia_si_todos_los_artistas_ya_estan_asignados() {
        val fixture = prepararEscenarioAlbumConTodosLosArtistasAsignados()

        try {
            abrirDetalleDeAlbum(fixture.albumName)
            abrirFormularioAgregarArtistaDesdeAlbum()
            esperarAdvertenciaAlbumArtist()

            composeTestRule.onNodeWithText("Este álbum ya tiene asignados todos los artistas disponibles").assertIsDisplayed()
            composeTestRule.onNodeWithText("Guardar").assertIsNotEnabled()
        } finally {
            limpiarAsociacionesAlbum(fixture.albumId)
        }
    }

    @Test
    fun desde_detalle_album_boton_atras_regresa_a_la_pantalla_anterior() {
        val fixture = prepararEscenarioAlbumConUnArtistaAsignado()

        try {
            abrirDetalleDeAlbum(fixture.albumName)
            abrirFormularioAgregarArtistaDesdeAlbum()
            esperarFormularioAlbumArtistConOpciones()

            composeTestRule.onNodeWithText("Atras").performClick()

            esperarDetalleAlbum()
            composeTestRule.onNodeWithText(fixture.albumName).assertIsDisplayed()
        } finally {
            limpiarAsociacionesAlbum(fixture.albumId)
        }
    }

    @Test
    fun desde_detalle_artista_permite_agregar_album_filtra_asignados_y_guarda() {
        val fixture = prepararEscenarioArtistaConUnAlbumAsignado()

        try {
            abrirDetalleDeArtista(fixture.artist.name)
            abrirFormularioAgregarAlbumDesdeArtista()
            esperarFormularioArtistAlbumConOpciones()

            composeTestRule.onNodeWithText("Guardar").assertIsNotEnabled()
            composeTestRule.onNodeWithTag("artist-album-dropdown-toggle", useUnmergedTree = true).performClick()
            composeTestRule.onAllNodesWithText(fixture.assignedAlbum.name).assertCountEquals(0)
            composeTestRule.onNodeWithText(fixture.availableAlbum.name).assertIsDisplayed()
            composeTestRule.onNodeWithText(fixture.availableAlbum.name).performClick()
            composeTestRule.onNodeWithText("Guardar").performClick()

            esperarDetalleArtista()
            composeTestRule.onNodeWithText(fixture.availableAlbum.name, substring = true).assertIsDisplayed()
        } finally {
            limpiarAsociacionesArtista(fixture.artist)
        }
    }

    @Test
    fun desde_detalle_artista_muestra_advertencia_si_todos_los_albumes_ya_estan_asignados() {
        val fixture = prepararEscenarioArtistaConTodosLosAlbumesAsignados()

        try {
            abrirDetalleDeArtista(fixture.artist.name)
            abrirFormularioAgregarAlbumDesdeArtista()
            esperarAdvertenciaArtistAlbum()

            composeTestRule.onNodeWithText("Este artista ya tiene asignados todos los álbumes disponibles").assertIsDisplayed()
            composeTestRule.onNodeWithText("Guardar").assertIsNotEnabled()
        } finally {
            limpiarAsociacionesArtista(fixture.artist)
        }
    }

    @Test
    fun desde_detalle_artista_boton_atras_regresa_a_la_pantalla_anterior() {
        val fixture = prepararEscenarioArtistaConUnAlbumAsignado()

        try {
            abrirDetalleDeArtista(fixture.artist.name)
            abrirFormularioAgregarAlbumDesdeArtista()
            esperarFormularioArtistAlbumConOpciones()

            composeTestRule.onNodeWithText("Atras").performClick()

            esperarDetalleArtista()
            composeTestRule.onNodeWithTag("artist-detail-name").assertIsDisplayed()
        } finally {
            limpiarAsociacionesArtista(fixture.artist)
        }
    }

    private fun prepararEscenarioAlbumConUnArtistaAsignado(): AlbumFlowFixture {
        val album = allAlbums.first()
        val existingArtist = allArtists.first()
        val availableArtist = allArtists.first { it != existingArtist }

        limpiarAsociacionesAlbum(album.id)
        asociarAlbumConArtista(existingArtist, album.id)
        esperarAlbumConArtistas(album.id, setOf(existingArtist.id to existingArtist.type))

        return AlbumFlowFixture(album.id, album.name, existingArtist, availableArtist)
    }

    private fun prepararEscenarioAlbumConTodosLosArtistasAsignados(): AlbumWarningFixture {
        val album = allAlbums.first()

        limpiarAsociacionesAlbum(album.id)
        allArtists.forEach { artist -> asociarAlbumConArtista(artist, album.id) }
        esperarAlbumConArtistas(album.id, allArtists.map { it.id to it.type }.toSet())

        return AlbumWarningFixture(album.id, album.name)
    }

    private fun prepararEscenarioArtistaConUnAlbumAsignado(): ArtistFlowFixture {
        val artist = allArtists.first()
        val assignedAlbum = allAlbums.first()
        val availableAlbum = allAlbums.first { it != assignedAlbum }

        limpiarAsociacionesArtista(artist)
        asociarAlbumConArtista(artist, assignedAlbum.id)
        esperarArtistaConAlbumes(artist, setOf(assignedAlbum.id))

        return ArtistFlowFixture(artist, assignedAlbum, availableAlbum)
    }

    private fun prepararEscenarioArtistaConTodosLosAlbumesAsignados(): ArtistWarningFixture {
        val artist = allArtists.first()

        limpiarAsociacionesArtista(artist)
        allAlbums.forEach { album -> asociarAlbumConArtista(artist, album.id) }
        esperarArtistaConAlbumes(artist, allAlbums.map { it.id }.toSet())

        return ArtistWarningFixture(artist)
    }

    private fun limpiarAsociacionesAlbum(albumId: Int) {
        allArtists.forEach { artist ->
            if (estaAlbumAsociadoAArtista(artist, albumId)) {
                deleteWithoutBody("/${artist.type.endpoint}/${artist.id}/albums/$albumId")
            }
        }
        esperarAlbumConArtistas(albumId, emptySet())
    }

    private fun limpiarAsociacionesArtista(artist: ArtistSelection) {
        val detail = getJsonObject("/${artist.type.endpoint}/${artist.id}")
        val albums = detail.optJSONArray("albums") ?: JSONArray()
        for (index in 0 until albums.length()) {
            val albumId = albums.getJSONObject(index).getInt("id")
            deleteWithoutBody("/${artist.type.endpoint}/${artist.id}/albums/$albumId")
        }
        esperarArtistaConAlbumes(artist, emptySet())
    }

    private fun estaAlbumAsociadoAArtista(artist: ArtistSelection, albumId: Int): Boolean {
        val detail = getJsonObject("/${artist.type.endpoint}/${artist.id}")
        val albums = detail.optJSONArray("albums") ?: JSONArray()
        return (0 until albums.length()).any { index ->
            albums.getJSONObject(index).getInt("id") == albumId
        }
    }

    private fun asociarAlbumConArtista(artist: ArtistSelection, albumId: Int) {
        if (!estaAlbumAsociadoAArtista(artist, albumId)) {
            postWithoutBody("/${artist.type.endpoint}/${artist.id}/albums/$albumId")
        }
    }

    private fun esperarAlbumConArtistas(albumId: Int, expectedArtists: Set<Pair<Int, ArtistEndpointType>>) {
        repeat(20) {
            val album = getJsonObject("/albums/$albumId")
            val performers = album.optJSONArray("performers") ?: JSONArray()
            val current = (0 until performers.length()).map { index ->
                val performer = performers.getJSONObject(index)
                val id = performer.getInt("id")
                val type = allArtists.firstOrNull { it.id == id && it.name == performer.getString("name") }?.type
                    ?: allArtists.firstOrNull { it.id == id }?.type
                    ?: ArtistEndpointType.MUSICIAN
                id to type
            }.toSet()
            if (current == expectedArtists) return
            Thread.sleep(300)
        }
        throw IllegalStateException("El album $albumId no alcanzo el estado esperado")
    }

    private fun esperarArtistaConAlbumes(artist: ArtistSelection, expectedAlbumIds: Set<Int>) {
        repeat(20) {
            val detail = getJsonObject("/${artist.type.endpoint}/${artist.id}")
            val albums = detail.optJSONArray("albums") ?: JSONArray()
            val current = (0 until albums.length()).map { index ->
                albums.getJSONObject(index).getInt("id")
            }.toSet()
            if (current == expectedAlbumIds) return
            Thread.sleep(300)
        }
        throw IllegalStateException("El artista ${artist.name} no alcanzo el estado esperado")
    }

    private fun abrirDetalleDeAlbum(albumName: String) {
        composeTestRule.onAllNodesWithText("Álbumes")[0].performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("album-item").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("album-filter-name").performTextInput(albumName)
        composeTestRule.onNodeWithTag("album-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText(albumName, substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("album-detail-button", useUnmergedTree = true)[0].performClick()
        esperarDetalleAlbum()
    }

    private fun abrirDetalleDeArtista(artistName: String) {
        composeTestRule.onAllNodesWithText("Artistas")[0].performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("artist-item").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("artist-filter-name").performTextInput(artistName)
        composeTestRule.onNodeWithTag("artist-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText(artistName, substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("artist-detail-button", useUnmergedTree = true)[0].performClick()
        esperarDetalleArtista()
    }

    private fun abrirFormularioAgregarArtistaDesdeAlbum() {
        composeTestRule.onNodeWithContentDescription("Agregar artista").performClick()
        composeTestRule.onNodeWithText("Agregar artista al álbum").assertIsDisplayed()
    }

    private fun abrirFormularioAgregarAlbumDesdeArtista() {
        composeTestRule.onNodeWithContentDescription("Agregar Album").performClick()
        composeTestRule.onNodeWithText("Agregar álbum al artista").assertIsDisplayed()
    }

    private fun esperarFormularioAlbumArtistConOpciones() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("album-artist-dropdown-toggle", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarAdvertenciaAlbumArtist() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText("Este álbum ya tiene asignados todos los artistas disponibles")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarFormularioArtistAlbumConOpciones() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("artist-album-dropdown-toggle", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarAdvertenciaArtistAlbum() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText("Este artista ya tiene asignados todos los álbumes disponibles")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarDetalleAlbum() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Lanzamiento:", substring = true).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarDetalleArtista() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("artist-detail-name").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun obtenerTodosLosArtistas(): List<ArtistSelection> {
        val bands = getJsonArray("/bands")
        val musicians = getJsonArray("/musicians")
        val artists = mutableListOf<ArtistSelection>()

        for (index in 0 until bands.length()) {
            val band = bands.getJSONObject(index)
            artists.add(
                ArtistSelection(
                    id = band.getInt("id"),
                    name = band.getString("name"),
                    type = ArtistEndpointType.BAND,
                    label = "${band.getString("name")} (Banda)"
                )
            )
        }

        for (index in 0 until musicians.length()) {
            val musician = musicians.getJSONObject(index)
            artists.add(
                ArtistSelection(
                    id = musician.getInt("id"),
                    name = musician.getString("name"),
                    type = ArtistEndpointType.MUSICIAN,
                    label = "${musician.getString("name")} (Músico)"
                )
            )
        }

        return artists.sortedBy { it.name }
    }

    private fun obtenerTodosLosAlbumes(): List<AlbumSelection> {
        val albums = getJsonArray("/albums")
        return (0 until albums.length()).map { index ->
            val album = albums.getJSONObject(index)
            AlbumSelection(id = album.getInt("id"), name = album.getString("name"))
        }.sortedBy { it.name }
    }

    private fun getJsonArray(path: String): JSONArray {
        val response = URL("$BASE_URL$path").readText()
        return JSONArray(response)
    }

    private fun getJsonObject(path: String): JSONObject {
        val response = URL("$BASE_URL$path").readText()
        return JSONObject(response)
    }

    private fun postWithoutBody(path: String) {
        requestWithoutBody("POST", path)
    }

    private fun deleteWithoutBody(path: String) {
        requestWithoutBody("DELETE", path)
    }

    private fun requestWithoutBody(method: String, path: String) {
        val connection = (URL("$BASE_URL$path").openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 10_000
            readTimeout = 10_000
        }

        try {
            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                val errorBody = connection.errorStream?.bufferedReader()?.use { reader -> reader.readText() }.orEmpty()
                throw IllegalStateException("$method $path fallo con codigo $responseCode. $errorBody")
            }
            connection.inputStream?.close()
        } finally {
            connection.disconnect()
        }
    }

    private data class AlbumFlowFixture(
        val albumId: Int,
        val albumName: String,
        val existingArtist: ArtistSelection,
        val availableArtist: ArtistSelection
    )

    private data class AlbumWarningFixture(
        val albumId: Int,
        val albumName: String
    )

    private data class ArtistFlowFixture(
        val artist: ArtistSelection,
        val assignedAlbum: AlbumSelection,
        val availableAlbum: AlbumSelection
    )

    private data class ArtistWarningFixture(
        val artist: ArtistSelection
    )

    private data class ArtistSelection(
        val id: Int,
        val name: String,
        val type: ArtistEndpointType,
        val label: String
    )

    private data class AlbumSelection(
        val id: Int,
        val name: String
    )

    private enum class ArtistEndpointType(val endpoint: String) {
        BAND("bands"),
        MUSICIAN("musicians")
    }

    private companion object {
        const val BASE_URL = "https://vinilos-backend-equipo6-db91c0ab96d3.herokuapp.com"
    }
}
