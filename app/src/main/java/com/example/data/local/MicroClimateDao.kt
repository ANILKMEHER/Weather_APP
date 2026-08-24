package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MicroClimateDao {
    @Query("SELECT * FROM weather_cache WHERE locationId = :locationId LIMIT 1")
    fun getWeatherForLocation(locationId: String): Flow<WeatherCacheEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWeather(weather: WeatherCacheEntity)

    @Query("SELECT * FROM timeline_hours WHERE locationId = :locationId ORDER BY hourOfDay ASC")
    fun getTimelineForLocation(locationId: String): Flow<List<TimelineHourEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeline(hours: List<TimelineHourEntity>)

    @Query("DELETE FROM timeline_hours WHERE locationId = :locationId")
    suspend fun clearTimeline(locationId: String)

    @Query("SELECT * FROM saved_places ORDER BY isDefault DESC, name ASC")
    fun getSavedPlaces(): Flow<List<SavedPlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPlace(place: SavedPlaceEntity)

    @Query("DELETE FROM saved_places WHERE placeId = :placeId")
    suspend fun deleteSavedPlace(placeId: String)

    @Query("SELECT * FROM cloud_sync_state WHERE id = 1 LIMIT 1")
    fun getCloudSyncState(): Flow<CloudSyncStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCloudSyncState(state: CloudSyncStateEntity)
}
