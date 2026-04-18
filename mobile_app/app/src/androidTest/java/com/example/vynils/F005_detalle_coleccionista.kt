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
class F005_detalle_coleccionista {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val collectorFixture by lazy { obtenerColeccionistaParaDetalle() }

    @Test
    fun muestra_nombre() {
        abrirDetalleDeColeccionista()

        composeTestRule.onNodeWithTag("collector-detail-name").assertIsDisplayed()
    }

    @Test
    fun muestra_lista_de_albumes_en_su_coleccion() {
        abrirDetalleDeColeccionista()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("album-item")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("album-item")[0].assertIsDisplayed()
    }

    @Test
    fun muestra_lista_de_artistas_favoritos() {
        abrirDetalleDeColeccionista()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("favorite-performer-item")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("favorite-performer-item")[0].assertIsDisplayed()
    }

    private fun abrirDetalleDeColeccionista() {
        composeTestRule.onAllNodesWithText("Coleccionistas")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Coleccionistas")[0].performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("collector-item")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("collector-filter-name").performTextInput(collectorFixture.nameFragment)
        composeTestRule.onNodeWithTag("collector-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText(collectorFixture.nameFragment, substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("collector-detail-button", useUnmergedTree = true)[0].performClick()
        composeTestRule.onNodeWithTag("collector-detail-name").assertIsDisplayed()
    }

    private fun obtenerColeccionistaParaDetalle(): CollectorFixture {
        val collectors = JSONArray(URL("http://10.0.2.2:3000/collectors").readText())
        val collector = (0 until collectors.length())
            .map { collectors.getJSONObject(it) }
            .firstOrNull { json ->
                val hasAlbums = json.optJSONArray("collectorAlbums")?.length()?.let { it > 0 } ?: false
                val hasFavorites = json.optJSONArray("favoritePerformers")?.length()?.let { it > 0 } ?: false
                hasAlbums && hasFavorites
            } ?: collectors.getJSONObject(0)

        return CollectorFixture(
            nameFragment = collector.getString("name").take(4)
        )
    }

    private data class CollectorFixture(
        val nameFragment: String
    )
}
