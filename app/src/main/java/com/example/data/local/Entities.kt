package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val locationId: String,
    val locationName: String,
    val region: String,
    val temperatureC: Int,
    val feelsLikeC: Int,
    val condition: String,
    val aqi: Int,
    val aqiStatus: String,
    val uvIndex: Int,
    val uvStatus: String,
    val precipitationChance: Int,
    val precipitationSummary: String,
    val aiInsight: String,
    val humidity: Int,
    val windSpeedKmh: Int,
    val pressureHpa: Int,
    val visibilityKm: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "timeline_hours")
data class TimelineHourEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val locationId: String,
    val timeLabel: String,       // e.g. "Now", "3:00 PM", "6:00 PM"
    val hourOfDay: Int,          // 0-23
    val conditionTitle: String,  // "Partly Cloudy", "Clear Skies", "Light Drizzle"
    val subtitle: String,        // "Comfortable for walking", "UV Index rising", "Carry an umbrella"
    val tempC: Int,
    val rainProb: Int,
    val aqi: Int,
    val isAlert: Boolean = false,
    val isPrimaryPill: Boolean = false
)

@Entity(tableName = "saved_places")
data class SavedPlaceEntity(
    @PrimaryKey val placeId: String,
    val name: String,
    val region: String,
    val tag: String, // "Home", "Work", "Commute", "Favorite"
    val lat: Double,
    val lon: Double,
    val isDefault: Boolean = false,
    val lastSynced: Long = System.currentTimeMillis()
)

@Entity(tableName = "cloud_sync_state")
data class CloudSyncStateEntity(
    @PrimaryKey val id: Int = 1,
    val deviceId: String,
    val lastSyncedTime: Long,
    val syncStatus: String, // "SYNCED", "SYNCING", "PENDING", "OFFLINE"
    val cloudSnapshotCount: Int = 1
)
