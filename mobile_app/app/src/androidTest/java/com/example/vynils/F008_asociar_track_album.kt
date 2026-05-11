package com.example.vynils

import androidx.compose.ui.test.assertCountEquals
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class F008_asociar_track_album {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun presenta_formulario_gestion_tracks_y_controles_requeridos() {
        abrirFormularioTracks()

        composeTestRule.onNodeWithText("Gestión de Tracks").assertIsDisplayed()
        composeTestRule.onNodeWithTag("track-form-name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("track-form-duration").assertIsDisplayed()
        composeTestRule.onNodeWithTag("track-form-list").assertIsDisplayed()
        composeTestRule.onNodeWithTag("track-form-save").assertIsDisplayed()
        composeTestRule.onNodeWithTag("track-form-cancel").assertIsDisplayed()
    }

    @Test
    fun permite_agregar_multiples_tracks_y_visualiza_opciones_edicion_y_eliminacion() {
        abrirFormularioTracks()

        composeTestRule.onNodeWithTag("track-form-name").performTextInput("Track A")
        composeTestRule.onNodeWithTag("track-form-duration").performTextInput("03:10")
        composeTestRule.onNodeWithTag("track-form-add").performClick()

        composeTestRule.onNodeWithTag("track-form-name").performTextInput("Track B")
        composeTestRule.onNodeWithTag("track-form-duration").performTextInput("04:20")
        composeTestRule.onNodeWithTag("track-form-add").performClick()

        composeTestRule.onAllNodesWithTag("track-item").assertCountEquals(2)
        composeTestRule.onAllNodesWithTag("track-item-edit").assertCountEquals(2)
        composeTestRule.onAllNodesWithTag("track-item-delete").assertCountEquals(2)
    }

    @Test
    fun guardar_valida_datos_asocia_tracks_y_muestra_feedback() {
        val uniqueTrack = "track-e2e-${System.currentTimeMillis()}"

        abrirFormularioTracks()

        composeTestRule.onNodeWithTag("track-form-name").performTextInput(uniqueTrack)
        composeTestRule.onNodeWithTag("track-form-duration").performTextInput("05:05")
        composeTestRule.onNodeWithTag("track-form-add").performClick()
        composeTestRule.onNodeWithTag("track-form-save").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("track-form-success").fetchSemanticsNodes().isNotEmpty() ||
                composeTestRule.onAllNodesWithTag("track-form-error").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("track-form-success").assertIsDisplayed()
    }

    @Test
    fun cancelar_descarta_cambios_y_retorna_a_la_vista_anterior() {
        abrirFormularioTracks()

        composeTestRule.onNodeWithTag("track-form-name").performTextInput("Track temporal")
        composeTestRule.onNodeWithTag("track-form-duration").performTextInput("02:30")
        composeTestRule.onNodeWithTag("track-form-add").performClick()
        composeTestRule.onNodeWithTag("track-form-cancel").performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Gestión de Tracks").fetchSemanticsNodes().isEmpty()
        }
    }

    private fun abrirFormularioTracks() {
        composeTestRule.onAllNodesWithText("Álbumes")[0].performClick()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("album-item").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithTag("album-detail-button", useUnmergedTree = true)[0].performClick()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Tracks").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithContentDescription("Agregar track").performClick()
    }
}
