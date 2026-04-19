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
class F003_consultar_coleccionistas {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val collectorFixture by lazy { obtenerPrimerColeccionista() }

    @Test
    fun muestra_lista_con_nombre() {
        abrirPantallaColeccionistas()
        esperarCargaInicial()

        composeTestRule.onAllNodesWithTag("collector-name", useUnmergedTree = true)[0].assertIsDisplayed()
    }

    @Test
    fun permite_filtrar_por_nombre() {
        abrirPantallaColeccionistas()
        esperarCargaInicial()

        composeTestRule.onNodeWithContentDescription("Filtrar").performClick()
        composeTestRule.onNodeWithTag("collector-filter-name").performTextInput(collectorFixture.nameFragment)
        composeTestRule.onNodeWithTag("collector-filter-apply").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText(collectorFixture.nameFragment, substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(collectorFixture.nameFragment, substring = true).assertIsDisplayed()
    }

    @Test
    fun cada_elemento_tiene_opcion_ver_detalle() {
        abrirPantallaColeccionistas()
        esperarCargaInicial()

        composeTestRule.onAllNodesWithTag("collector-detail-button", useUnmergedTree = true)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("collector-detail-button", useUnmergedTree = true)[0].performClick()

        esperarDetalleColeccionista()
        composeTestRule.onNodeWithTag("collector-detail-name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Teléfono:", substring = true).assertIsDisplayed()
    }

    private fun abrirPantallaColeccionistas() {
        composeTestRule.onAllNodesWithText("Coleccionistas")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Coleccionistas")[0].performClick()
    }

    private fun esperarCargaInicial() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("collector-item")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun esperarDetalleColeccionista() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("collector-detail-name")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun obtenerPrimerColeccionista(): CollectorFixture {
        val response = URL("http://10.0.2.2:3000/collectors").readText()
        val collectors = JSONArray(response)
        val collector = collectors.getJSONObject(0)

        return CollectorFixture(
            nameFragment = collector.getString("name").take(4)
        )
    }

    private data class CollectorFixture(
        val nameFragment: String
    )
}
