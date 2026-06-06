package de.pdfwerkzeugkasten

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class MainFlowSmokeTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    @Test fun onboardingOrHomeIsVisible() {
        try { compose.onNodeWithText("PDF Werkzeugkasten").assertIsDisplayed() } catch (_: AssertionError) { compose.onNodeWithText("Deine PDF-Werkzeuge an einem Ort").assertIsDisplayed() }
    }
}
