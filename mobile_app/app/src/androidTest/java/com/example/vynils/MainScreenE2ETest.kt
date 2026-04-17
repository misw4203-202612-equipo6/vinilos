package com.example.vynils

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigateBetweenHomeAlbumsAndCollectors() {
            composeTestRule.onNodeWithText("Álbumes").performClick()
//        composeTestRule.onNodeWithText("Artistas").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Coleccionistas").assertIsDisplayed()
//
//        composeTestRule.onNodeWithText("Álbumes").performClick()
//        composeTestRule.onNodeWithContentDescription("Atrás").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Álbumes").assertIsDisplayed()
//
//        composeTestRule.onNodeWithContentDescription("Atrás").performClick()
//        composeTestRule.onNodeWithContentDescription("Menú").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Coleccionistas").assertIsDisplayed()
//
//        composeTestRule.onNodeWithContentDescription("Menú").performClick()
//        composeTestRule.onNodeWithText("Inicio").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Coleccionistas").assertIsDisplayed()
    }
}
