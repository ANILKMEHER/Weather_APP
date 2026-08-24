package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CloudSyncStateEntity
import com.example.data.local.SavedPlaceEntity
import com.example.data.local.TimelineHourEntity
import com.example.data.local.WeatherCacheEntity
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class AppTab(val title: String, val icon: String) {
    TODAY("Today", "today"),
    FORECAST("Forecast", "wb_sunny"),
    MAP("Map", "map"),
    SETTINGS("Settings", "settings")
}

data class UiState(
    val currentTab: AppTab = AppTab.TODAY,
    val isDarkMode: Boolean = false,
    val selectedLocationId: String = "pune_pimple_saudagar",
    val weather: WeatherCacheEntity? = null,
    val timeline: List<TimelineHourEntity> = emptyList(),
    val savedPlaces: List<SavedPlaceEntity> = emptyList(),
    val cloudSyncState: CloudSyncStateEntity? = null,
    val isSyncing: Boolean = false,
    val aiInsightFeedback: Boolean? = null, // null, true = liked, false = disliked
    val aiGenerating: Boolean = false,
    val aiAssistantQuery: String = "",
    val aiAssistantResponse: String = "",
    val showLocationPicker: Boolean = false,
    val showAssistantSheet: Boolean = false,
    val showTimelineDetailSheet: Boolean = false,
    val showNotificationsSheet: Boolean = false,
    val temperatureUnit: String = "°C", // "°C" or "°F"
    val windUnit: String = "km/h",
    val autoSyncEnabled: Boolean = true,
    val notificationBadgeCount: Int = 2
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = WeatherRepository(database)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
            observeData()
        }
    }

    private fun observeData() {
        // Observe current weather
        viewModelScope.launch {
            _uiState.collectLatest { state ->
                repository.getWeather(state.selectedLocationId).collect { weather ->
                    _uiState.value = _uiState.value.copy(weather = weather)
                }
            }
        }

        // Observe timeline
        viewModelScope.launch {
            _uiState.collectLatest { state ->
                repository.getTimeline(state.selectedLocationId).collect { timeline ->
                    _uiState.value = _uiState.value.copy(timeline = timeline)
                }
            }
        }

        // Observe saved places
        viewModelScope.launch {
            repository.getSavedPlaces().collect { places ->
                _uiState.value = _uiState.value.copy(savedPlaces = places)
            }
        }

        // Observe cloud sync state
        viewModelScope.launch {
            repository.getCloudSyncState().collect { syncState ->
                _uiState.value = _uiState.value.copy(cloudSyncState = syncState)
            }
        }
    }

    fun setTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun toggleDarkMode() {
        _uiState.value = _uiState.value.copy(isDarkMode = !_uiState.value.isDarkMode)
    }

    fun setDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkMode = enabled)
    }

    fun selectLocation(place: SavedPlaceEntity) {
        _uiState.value = _uiState.value.copy(
            selectedLocationId = place.placeId,
            showLocationPicker = false
        )
    }

    fun setInsightFeedback(liked: Boolean) {
        _uiState.value = _uiState.value.copy(aiInsightFeedback = liked)
    }

    fun triggerCloudSync() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true)
            repository.performCloudSync()
            _uiState.value = _uiState.value.copy(isSyncing = false)
        }
    }

    fun toggleAutoSync(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoSyncEnabled = enabled)
    }

    fun toggleTemperatureUnit() {
        val next = if (_uiState.value.temperatureUnit == "°C") "°F" else "°C"
        _uiState.value = _uiState.value.copy(temperatureUnit = next)
    }

    fun toggleLocationPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showLocationPicker = show)
    }

    fun toggleAssistantSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAssistantSheet = show)
    }

    fun toggleTimelineDetail(show: Boolean) {
        _uiState.value = _uiState.value.copy(showTimelineDetailSheet = show)
    }

    fun toggleNotifications(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showNotificationsSheet = show,
            notificationBadgeCount = if (show) 0 else _uiState.value.notificationBadgeCount
        )
    }

    fun setAssistantQuery(query: String) {
        _uiState.value = _uiState.value.copy(aiAssistantQuery = query)
    }

    fun askGemini(customPrompt: String? = null) {
        val query = customPrompt ?: _uiState.value.aiAssistantQuery
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(aiGenerating = true, aiAssistantQuery = query)
            val weather = _uiState.value.weather
            val location = weather?.locationName ?: "Pimple Saudagar, Pune"
            val temp = weather?.temperatureC ?: 28
            val aqi = weather?.aqi ?: 160

            val result = repository.fetchGeminiWeatherIntelligence(
                userQuery = query,
                location = location,
                currentTemp = temp,
                aqi = aqi
            )

            _uiState.value = _uiState.value.copy(
                aiGenerating = false,
                aiAssistantResponse = result
            )
        }
    }

    fun addCustomLocation(name: String, region: String, tag: String) {
        viewModelScope.launch {
            val id = name.lowercase().replace(" ", "_").replace(",", "")
            val newPlace = SavedPlaceEntity(
                placeId = id,
                name = name,
                region = region,
                tag = tag,
                lat = 18.5204,
                lon = 73.8567
            )
            repository.savePlace(newPlace)
            triggerCloudSync()
        }
    }

    fun removeCustomLocation(placeId: String) {
        viewModelScope.launch {
            repository.removePlace(placeId)
            triggerCloudSync()
        }
    }
}
