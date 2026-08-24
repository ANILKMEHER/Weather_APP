package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.ClimatoIQTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      ClimatoIQTheme {
        TodayScreen(
          weather = null,
          timeline = emptyList(),
          isDarkMode = false,
          temperatureUnit = "°C",
          feedbackState = null,
          notificationBadgeCount = 2,
          onToggleDarkMode = {},
          onOpenLocationPicker = {},
          onOpenNotifications = {},
          onOpenAiAssistant = {},
          onOpenTimelineDetail = {},
          onFeedback = {},
          onHourSelected = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
