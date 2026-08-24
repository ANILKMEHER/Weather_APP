package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BentoCard
import com.example.ui.components.getConditionIcon
import com.example.ui.theme.LocalExtraColors

data class DayForecast(
    val dayName: String,
    val dateStr: String,
    val condition: String,
    val highTemp: Int,
    val lowTemp: Int,
    val rainChance: Int,
    val aqi: Int,
    val windKmh: Int,
    val aiBriefing: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    locationName: String,
    temperatureUnit: String,
    onOpenAiAssistant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extraColors = LocalExtraColors.current
    var selectedDayIndex by remember { mutableStateOf(0) }

    val days = remember {
        listOf(
            DayForecast("Today", "Aug 24", "Partly Cloudy", 31, 22, 20, 160, 14, "Afternoon heat peak with brief localized drizzle around 6 PM."),
            DayForecast("Tue", "Aug 25", "Scattered Rain", 28, 21, 65, 120, 18, "Consistent overcast sky with moderate afternoon showers."),
            DayForecast("Wed", "Aug 26", "Thunderstorm", 27, 20, 80, 85, 22, "Early evening lightning window; plan indoor transit routes."),
            DayForecast("Thu", "Aug 27", "Light Drizzle", 29, 21, 35, 110, 12, "Rapid clearing by 4 PM; great air quality recovery."),
            DayForecast("Fri", "Aug 28", "Clear Skies", 32, 23, 10, 145, 10, "High UV intensity during midday; apply sun protection."),
            DayForecast("Sat", "Aug 29", "Partly Cloudy", 30, 22, 15, 130, 15, "Pleasant weekend micro-climate; ideal for morning outdoor sports."),
            DayForecast("Sun", "Aug 30", "Sunny & Warm", 33, 24, 5, 150, 11, "Elevated heat index in concrete urban corridors.")
        )
    }

    val selectedDay = days[selectedDayIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "7-Day Micro-Forecast",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = locationName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = extraColors.bentoLavender,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = extraColors.bentoDeepPurple,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "AI Synced",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = extraColors.bentoDeepPurple
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Selected Day AI Deep-Dive Card
            item {
                BentoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forecast_ai_deep_dive"),
                    backgroundColor = extraColors.bentoDeepPurple,
                    borderColor = extraColors.bentoBorder.copy(alpha = 0.4f),
                    contentPadding = 20.dp,
                    onClick = onOpenAiAssistant
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = extraColors.bentoLavender,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "${selectedDay.dayName} Smart Briefing",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = extraColors.bentoLavender
                                    )
                                )
                            }
                            Text(
                                text = selectedDay.dateStr,
                                style = MaterialTheme.typography.labelMedium,
                                color = extraColors.bentoLavender.copy(alpha = 0.8f)
                            )
                        }

                        Text(
                            text = selectedDay.aiBriefing,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }

            // Hourly Rain Probability Bar Visualizer
            item {
                BentoCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    borderColor = extraColors.bentoBorder,
                    contentPadding = 18.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Precipitation Probability Trend",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = extraColors.bentoLavender
                            )
                        )

                        val hours = listOf("12 PM" to 10, "2 PM" to 15, "4 PM" to 25, "6 PM" to 75, "8 PM" to 40, "10 PM" to 15, "12 AM" to 5)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            hours.forEach { (hr, prob) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "$prob%",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (prob > 50) extraColors.bentoLavender else MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(18.dp)
                                            .height((prob * 0.7f).coerceAtLeast(6f).dp)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(
                                                if (prob > 50) extraColors.bentoLavender
                                                else extraColors.bentoLavender.copy(alpha = 0.25f)
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = hr,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 7 Days List
            items(days.size) { index ->
                val day = days[index]
                val isSelected = index == selectedDayIndex

                BentoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forecast_row_$index"),
                    backgroundColor = if (isSelected) extraColors.bentoLilac else MaterialTheme.colorScheme.surfaceContainerLow,
                    borderColor = if (isSelected) Color.Transparent else extraColors.bentoBorder,
                    contentPadding = 14.dp,
                    onClick = { selectedDayIndex = index }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getConditionIcon(day.condition),
                                    contentDescription = day.condition,
                                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = day.dayName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = day.condition,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        // Metrics (Rain + Temp range)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (day.rainChance > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "Rain",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "${day.rainChance}%",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            // Temperature High / Low
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "${day.highTemp}°",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${day.lowTemp}°",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
