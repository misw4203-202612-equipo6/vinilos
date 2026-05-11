package com.example.vynils

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class F007_crear_album {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun presenta_formulario_con_campos_y_controles_requeridos() {
        abrirFormularioCrearAlbum()

        composeTestRule.onNodeWithText("Agregar Álbum").assertIsDisplayed()
        composeTestRule.onNodeWithTag("album-create-name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("album-create-artist").assertIsDisplayed()
        composeTestRule.onNodeWithTag("album-create-year").assertIsDisplayed()
        composeTestRule.onNodeWithTag("album-create-genre").assertIsDisplayed()
        composeTestRule.onNodeWithTag("album-create-save").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("album-create-cancel").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun guardar_valida_campos_obligatorios() {
        abrirFormularioCrearAlbum()

        composeTestRule.onNodeWithTag("album-create-save").performScrollTo().performClick()

        composeTestRule.onNodeWithTag("album-create-validation-error").assertIsDisplayed()
    }

    @Test
    fun guardar_con_datos_validos_registra_album_y_muestra_feedback() {
        val uniqueName = "e2e-album-${System.currentTimeMillis()}"

        eliminarAlbumesPorNombre(uniqueName)

        try {
            abrirFormularioCrearAlbum()
            esperarCargaArtistas()

            composeTestRule.onNodeWithTag("album-create-name").performTextInput(uniqueName)
        composeTestRule.onNodeWithTag("album-create-artist").performClick()
        composeTestRule.onNodeWithText("Queen").performClick()
        composeTestRule.onNodeWithTag("album-create-year").performTextInput("2026")
        composeTestRule.onNodeWithTag("album-create-genre").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Rock").performClick()
        composeTestRule.onNodeWithTag("album-create-save").performScrollTo().performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("album-create-success").fetchSemanticsNodes().isNotEmpty() ||
                composeTestRule.onAllNodesWithTag("album-create-error").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("album-create-success").assertIsDisplayed()
        } finally {
            eliminarAlbumesPorNombre(uniqueName)
        }
    }

    @Test
    fun cancelar_descarta_datos_y_retorna_a_la_vista_anterior() {
        val draftName = "album-borrador"

        abrirFormularioCrearAlbum()
        esperarCargaArtistas()

        composeTestRule.onNodeWithTag("album-create-name").performTextInput(draftName)
        composeTestRule.onNodeWithTag("album-create-cancel").performScrollTo().performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Agregar Álbum").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onAllNodesWithText(draftName, substring = true).assertCountEquals(0)
    }

    private fun abrirFormularioCrearAlbum() {
        composeTestRule.onAllNodesWithText("Álbumes")[0].performClick()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("album-item").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithContentDescription("Agregar").performClick()
        composeTestRule.onNodeWithText("Agregar Álbum").assertIsDisplayed()
    }

    private fun esperarCargaArtistas() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Guardar").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun eliminarAlbumesPorNombre(name: String) {
        val albums = JSONArray(URL("$BASE_URL/albums").readText())
        for (index in 0 until albums.length()) {
            val album = albums.getJSONObject(index)
            if (album.getString("name") == name) {
                deleteAlbum(album.getInt("id"))
            }
        }
    }

    private fun deleteAlbum(albumId: Int) {
        val connection = (URL("$BASE_URL/albums/$albumId").openConnection() as HttpURLConnection).apply {
            requestMethod = "DELETE"
            connectTimeout = 10_000
            readTimeout = 10_000
        }

        try {
            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
                throw IllegalStateException("DELETE /albums/$albumId fallo con codigo $responseCode. $errorBody")
            }
        } finally {
            connection.disconnect()
        }
    }

    private companion object {
        const val BASE_URL = "https://vinilos-backend-equipo6-db91c0ab96d3.herokuapp.com"
    }
}
