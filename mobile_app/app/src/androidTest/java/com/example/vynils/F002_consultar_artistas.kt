package com.example.vynils

import androidx.compose.ui.test.assertIsDisplayed
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
class F002_consultar_artistas {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val artistFixture by lazy { obtenerPrimerArtista() }

    @Test
    fun muestra_lista_con_nombre_y_foto() {
        abrirPantallaArtistas()
        esperarCargaInicial()

        composeTestRule.onAllNodesWithTag("artist-name", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("artist-photo", useUnmergedTree = true)[0].assertIsDisplayed()
    }

    @Test
    fun permite_filtrar_por_nombre() {
        abrirPantallaArtistas()
        esperarCargaInicial()

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("artist-filter-name").performTextInput(artistFixture.nameFragment)
        composeTestRule.onNodeWithTag("artist-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText(artistFixture.nameFragment, substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(artistFixture.nameFragment, substring = true).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("artist-photo", useUnmergedTree = true)[0].assertIsDisplayed()
    }

    @Test
    fun cada_elemento_tiene_opcion_ver_detalle() {
        abrirPantallaArtistas()
        esperarCargaInicial()

        composeTestRule.onAllNodesWithTag("artist-detail-button", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("artist-detail-button", useUnmergedTree = true)[0].performClick()

        esperarDetalleArtista()
        composeTestRule.onNodeWithTag("artist-detail-name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Biografía").assertIsDisplayed()
    }

    private fun abrirPantallaArtistas() {
        composeTestRule.onAllNodesWithText("Artistas")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Artistas")[0].performClick()
    }

    private fun esperarCargaInicial() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("artist-item")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarDetalleArtista() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("artist-detail-name")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun obtenerPrimerArtista(): ArtistFixture {
        val response = URL("http://10.0.2.2:3000/bands").readText()
        val artists = JSONArray(response)
        val artist = artists.getJSONObject(0)
        println(artist)

        return ArtistFixture(
            nameFragment = artist.getString("name").take(4)
        )
    }

    private data class ArtistFixture(
        val nameFragment: String
    )
}
