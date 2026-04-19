package com.example.vynils

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.net.URL
import org.json.JSONArray
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class F001_consultar_albumes {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val albumFixture by lazy { obtenerPrimerAlbum() }

    @Test
    fun muestra_lista_con_nombre_artista_y_portada() {
        abrirPantallaAlbumes()
        esperarCargaInicial()

        composeTestRule.onAllNodesWithTag("album-name", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("album-artist", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("album-cover", useUnmergedTree = true)[0].assertIsDisplayed()
    }

    @Test
    fun permite_filtrar_por_nombre_genero_y_anio() {
        abrirPantallaAlbumes()
        esperarCargaInicial()

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("album-filter-name").performTextInput(albumFixture.nameFragment)
        composeTestRule.onNodeWithTag("album-filter-genre").performTextInput(albumFixture.genre)
        composeTestRule.onNodeWithTag("album-filter-year").performTextInput(albumFixture.year)
        composeTestRule.onNodeWithTag("album-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText(albumFixture.nameFragment, substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(albumFixture.nameFragment, substring = true).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("album-artist", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("album-cover", useUnmergedTree = true)[0].assertIsDisplayed()
    }

    @Test
    fun cada_elemento_tiene_opcion_ver_detalle() {
        abrirPantallaAlbumes()
        esperarCargaInicial()

        composeTestRule.onAllNodesWithTag("album-detail-button", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("album-detail-button", useUnmergedTree = true)[0].performClick()

        esperarDetalleAlbum()
        composeTestRule.onNodeWithText("Lanzamiento:", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Género:", substring = true).assertIsDisplayed()
    }

    private fun abrirPantallaAlbumes() {
        composeTestRule.onAllNodesWithText("Álbumes")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Álbumes")[0].performClick()
    }

    private fun esperarCargaInicial() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("album-item")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarDetalleAlbum() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText("Lanzamiento:", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun obtenerPrimerAlbum(): AlbumFixture {
        val response = URL("http://10.0.2.2:3000/albums").readText()
        val albums = JSONArray(response)
        val album = albums.getJSONObject(0)

        return AlbumFixture(
            nameFragment = album.getString("name").take(4),
            genre = album.getString("genre"),
            year = album.getString("releaseDate").take(4)
        )
    }

    private data class AlbumFixture(
        val nameFragment: String,
        val genre: String,
        val year: String
    )
}
