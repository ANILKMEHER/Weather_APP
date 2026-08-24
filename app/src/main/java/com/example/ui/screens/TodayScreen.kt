package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedPlaceEntity
import com.example.data.local.TimelineHourEntity
import com.example.data.local.WeatherCacheEntity
import com.example.ui.components.AiInsightCard
import com.example.ui.components.BentoCard
import com.example.ui.components.MicroClimateTimeline
import com.example.ui.components.WeatherBentoGrid
import com.example.ui.components.motion.WeatherAnimType
import com.example.ui.components.motion.WeatherBackgroundEffect
import com.example.ui.theme.LocalExtraColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    weather: WeatherCacheEntity?,
    timeline: List<TimelineHourEntity>,
    isDarkMode: Boolean,
    temperatureUnit: String,
    feedbackState: Boolean?,
    notificationBadgeCount: Int,
    onToggleDarkMode: () -> Unit,
    onOpenLocationPicker: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenAiAssistant: () -> Unit,
    onOpenTimelineDetail: () -> Unit,
    onFeedback: (Boolean) -> Unit,
    onHourSelected: (TimelineHourEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val locationName = weather?.locationName ?: "Pimple Saudagar, Pune"
    val aiInsightText = weather?.aiInsight ?: "Expect warm afternoon peaks; carry rain protection for your 6 PM transit window."
    val extraColors = LocalExtraColors.current

    Box(modifier = modifier.fillMaxSize()) {
        // Subtle background weather animation
        WeatherBackgroundEffect(
            type = if (weather?.precipitationChance ?: 0 > 30) WeatherAnimType.DRIZZLE else WeatherAnimType.SUNNY_SPECS
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Rounded-2xl avatar icon badge in Lavender
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(extraColors.bentoLavender)
                                    .clickable { onOpenAiAssistant() }
                                    .testTag("btn_cloud_logo"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "C",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 20.sp,
                                        color = extraColors.bentoDeepPurple
                                    )
                                )
                            }

                            Column {
                                Text(
                                    text = "ClimatoIQ",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        letterSpacing = (-0.3).sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(extraColors.connectedGreen)
                                    )
                                    Text(
                                        text = "CLOUD CONNECTED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.2.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        // Location Pin button (Sleek circular Bento button)
                        IconButton(
                            onClick = onOpenLocationPicker,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .testTag("btn_location_picker")
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Change Location",
                                tint = extraColors.bentoLavender,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Notifications Icon with indicator
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .testTag("btn_notifications")
                        ) {
                            Box {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                if (notificationBadgeCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.error)
                                    )
                                }
                            }
                        }

                        // Dark Mode Toggle (Sleek dark circular button matching design spec)
                        IconButton(
                            onClick = onToggleDarkMode,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .testTag("btn_toggle_dark_mode")
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Dark Mode",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mobile Location Pill (Centered Bento Pill)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, extraColors.bentoBorder.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onOpenLocationPicker() }
                        .testTag("pill_location_selector")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = extraColors.bentoLavender,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = locationName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                // AI Briefing Card
                AiInsightCard(
                    insightText = aiInsightText,
                    feedbackState = feedbackState,
                    onFeedback = onFeedback,
                    onCardClick = onOpenAiAssistant
                )

                // Bento Metrics Grid (Temperature, Air Quality, UV Index, Precipitation)
                WeatherBentoGrid(
                    weather = weather,
                    temperatureUnit = temperatureUnit,
                    onPrecipitationClick = onOpenTimelineDetail,
                    onAqiClick = onOpenAiAssistant,
                    onUvClick = onOpenAiAssistant,
                    onTempClick = onOpenTimelineDetail
                )

                // Micro-Climate Timeline Horizontal Carousel
                MicroClimateTimeline(
                    timeline = timeline,
                    onViewAllClick = onOpenTimelineDetail,
                    onHourClick = onHourSelected
                )

                // Quick Gemini Intelligence Bar
                BentoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .testTag("card_quick_gemini_bar"),
                    backgroundColor = extraColors.bentoDeepPurple.copy(alpha = 0.35f),
                    borderColor = extraColors.bentoBorder.copy(alpha = 0.4f),
                    contentPadding = 16.dp,
                    onClick = onOpenAiAssistant
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoLavender.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = extraColors.bentoLavender,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ask Gemini Weather Intelligence",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = extraColors.bentoLavender
                                )
                            )
                            Text(
                                text = "Transit windows, outfit planner, micro-climate Q&A...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
