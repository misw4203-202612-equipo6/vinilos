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
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.net.URL
import org.json.JSONException
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class F004_detalle_artista {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val performerFixture by lazy { obtenerPerformerParaDetalle() }

    @Test
    fun muestra_nombre_imagen_y_biografia() {
        abrirDetalleDeArtista()

        composeTestRule.onNodeWithTag("artist-detail-name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("artist-detail-image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("artist-detail-biography").assertIsDisplayed()
    }

    @Test
    fun muestra_lista_de_albumes_del_artista() {
        abrirDetalleDeArtista()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("album-item")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("album-item")[0].assertIsDisplayed()
    }

    @Test
    fun muestra_lista_de_tracks_de_forma_simplificada() {
        abrirDetalleDeArtista()
        desplazarHastaTracks()

        composeTestRule.onAllNodesWithTag("artist-tracks-title")[0].assertIsDisplayed()

        if (performerFixture.hasTracks) {
            composeTestRule.waitUntil(timeoutMillis = 10_000) {
                composeTestRule
                    .onAllNodesWithTag("artist-track-item")
                    .fetchSemanticsNodes().isNotEmpty()
            }
        } else {
            composeTestRule.onNodeWithText("No hay tracks disponibles").assertIsDisplayed()
        }
    }

    @Test
    fun muestra_lista_de_musicos_si_el_artista_es_agrupacion() {
        assumeTrue(performerFixture.hasMusicians)

        abrirDetalleDeArtista()
        desplazarHastaTracks()
        composeTestRule.onNodeWithTag("artist-detail-scroll").performTouchInput { swipeUp() }

        composeTestRule.onNodeWithText("Músicos").assertIsDisplayed()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("musician-item")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun abrirDetalleDeArtista() {
        composeTestRule.onAllNodesWithText("Artistas")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Artistas")[0].performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("artist-item")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("artist-filter-name").performTextInput(performerFixture.nameFragment)
        composeTestRule.onNodeWithTag("artist-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText(performerFixture.nameFragment, substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("artist-detail-button", useUnmergedTree = true)[0].performClick()
        esperarDetalleArtista()
        composeTestRule.onNodeWithTag("artist-detail-name").assertIsDisplayed()
    }

    private fun desplazarHastaTracks() {
        repeat(8) {
            if (composeTestRule.onAllNodesWithTag("artist-tracks-title").fetchSemanticsNodes().isNotEmpty()) {
                try {
                    composeTestRule.onAllNodesWithTag("artist-tracks-title")[0].assertIsDisplayed()
                    return
                } catch (_: AssertionError) {
                    // Keep scrolling until the section is actually visible.
                }
            }
            composeTestRule.onNodeWithTag("artist-detail-scroll").performTouchInput { swipeUp() }
        }
    }

    private fun esperarDetalleArtista() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("artist-detail-name")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun obtenerPerformerParaDetalle(): PerformerFixture {
        val bands = JSONArray(URL("http://10.0.2.2:3000/bands").readText())
        val musicians = JSONArray(URL("http://10.0.2.2:3000/musicians").readText())

        val performers = mutableListOf<JSONObject>()
        for (index in 0 until bands.length()) {
            performers.add(bands.getJSONObject(index))
        }
        for (index in 0 until musicians.length()) {
            performers.add(musicians.getJSONObject(index))
        }
        if (performers.isEmpty()) {
            throw IllegalStateException("No hay performers disponibles para la prueba")
        }

        // Prefer a performer with albums so the detail assertions are meaningful.
        val performer = performers.firstOrNull { performerJson ->
            performerJson.optJSONArray("albums")?.length()?.let { it > 0 } ?: false
        } ?: performers.first()

        val hasMusicians = performer.optJSONArray("musicians")?.length()?.let { it > 0 } ?: false
        val albums = performer.optJSONArray("albums")
        val hasTracks = albums?.let { albumsArray ->
            (0 until albumsArray.length()).any { index ->
                val album = albumsArray.getJSONObject(index)
                runCatching {
                    val albumDetail = JSONObject(URL("http://10.0.2.2:3000/albums/${album.getInt("id")}").readText())
                    albumDetail.optJSONArray("tracks")?.length()?.let { it > 0 } ?: false
                }.getOrDefault(false)
            }
        } ?: false

        return PerformerFixture(
            nameFragment = performer.getString("name").take(4),
            hasMusicians = hasMusicians,
            hasTracks = hasTracks
        )
    }

    private data class PerformerFixture(
        val nameFragment: String,
        val hasMusicians: Boolean,
        val hasTracks: Boolean
    )
}
