package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ClimatoBottomNav
import com.example.ui.components.dialogs.GeminiAssistantSheet
import com.example.ui.components.dialogs.LocationPickerDialog
import com.example.ui.components.dialogs.NotificationsSheet
import com.example.ui.components.dialogs.TimelineDetailDialog
import com.example.ui.screens.ForecastScreen
import com.example.ui.screens.MapScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.ClimatoIQTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ClimatoIQTheme(darkTheme = uiState.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        ClimatoBottomNav(
                            currentTab = uiState.currentTab,
                            onTabSelected = { viewModel.setTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(
                            targetState = uiState.currentTab,
                            animationSpec = tween(durationMillis = 220),
                            label = "tabCrossfade"
                        ) { tab ->
                            when (tab) {
                                AppTab.TODAY -> TodayScreen(
                                    weather = uiState.weather,
                                    timeline = uiState.timeline,
                                    isDarkMode = uiState.isDarkMode,
                                    temperatureUnit = uiState.temperatureUnit,
                                    feedbackState = uiState.aiInsightFeedback,
                                    notificationBadgeCount = uiState.notificationBadgeCount,
                                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                                    onOpenLocationPicker = { viewModel.toggleLocationPicker(true) },
                                    onOpenNotifications = { viewModel.toggleNotifications(true) },
                                    onOpenAiAssistant = { viewModel.toggleAssistantSheet(true) },
                                    onOpenTimelineDetail = { viewModel.toggleTimelineDetail(true) },
                                    onFeedback = { viewModel.setInsightFeedback(it) },
                                    onHourSelected = { viewModel.toggleTimelineDetail(true) }
                                )
                                AppTab.FORECAST -> ForecastScreen(
                                    locationName = uiState.weather?.locationName ?: "Pimple Saudagar, Pune",
                                    temperatureUnit = uiState.temperatureUnit,
                                    onOpenAiAssistant = { viewModel.toggleAssistantSheet(true) }
                                )
                                AppTab.MAP -> MapScreen(
                                    locationName = uiState.weather?.locationName ?: "Pimple Saudagar, Pune",
                                    onOpenAiAssistant = { viewModel.toggleAssistantSheet(true) }
                                )
                                AppTab.SETTINGS -> SettingsScreen(
                                    isDarkMode = uiState.isDarkMode,
                                    temperatureUnit = uiState.temperatureUnit,
                                    autoSyncEnabled = uiState.autoSyncEnabled,
                                    isSyncing = uiState.isSyncing,
                                    cloudSyncState = uiState.cloudSyncState,
                                    savedPlaces = uiState.savedPlaces,
                                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                                    onToggleTemperatureUnit = { viewModel.toggleTemperatureUnit() },
                                    onToggleAutoSync = { viewModel.toggleAutoSync(it) },
                                    onTriggerCloudSync = { viewModel.triggerCloudSync() },
                                    onRemovePlace = { viewModel.removeCustomLocation(it) }
                                )
                            }
                        }

                        // Sheets & Dialogs
                        if (uiState.showLocationPicker) {
                            LocationPickerDialog(
                                savedPlaces = uiState.savedPlaces,
                                selectedPlaceId = uiState.selectedLocationId,
                                onSelectPlace = { viewModel.selectLocation(it) },
                                onAddNewPlace = { name, reg, tag -> viewModel.addCustomLocation(name, reg, tag) },
                                onDismiss = { viewModel.toggleLocationPicker(false) }
                            )
                        }

                        if (uiState.showAssistantSheet) {
                            GeminiAssistantSheet(
                                queryText = uiState.aiAssistantQuery,
                                responseText = uiState.aiAssistantResponse,
                                isGenerating = uiState.aiGenerating,
                                onQueryChanged = { viewModel.setAssistantQuery(it) },
                                onSendQuery = { viewModel.askGemini(it) },
                                onDismiss = { viewModel.toggleAssistantSheet(false) }
                            )
                        }

                        if (uiState.showTimelineDetailSheet) {
                            TimelineDetailDialog(
                                timeline = uiState.timeline,
                                locationName = uiState.weather?.locationName ?: "Pimple Saudagar, Pune",
                                onDismiss = { viewModel.toggleTimelineDetail(false) }
                            )
                        }

                        if (uiState.showNotificationsSheet) {
                            NotificationsSheet(
                                onDismiss = { viewModel.toggleNotifications(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
