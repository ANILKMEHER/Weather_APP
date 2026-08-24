package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WeatherCacheEntity
import com.example.ui.components.motion.AnimatedSunSphere
import com.example.ui.theme.LocalExtraColors

@Composable
fun WeatherBentoGrid(
    weather: WeatherCacheEntity?,
    temperatureUnit: String = "°C",
    onPrecipitationClick: () -> Unit,
    onAqiClick: () -> Unit,
    onUvClick: () -> Unit,
    onTempClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extraColors = LocalExtraColors.current
    val tempVal = if (weather != null) {
        if (temperatureUnit == "°C") weather.temperatureC else ((weather.temperatureC * 9 / 5) + 32)
    } else 28
    val feelsLikeVal = if (weather != null) {
        if (temperatureUnit == "°C") weather.feelsLikeC else ((weather.feelsLikeC * 9 / 5) + 32)
    } else 31
    val aqiVal = weather?.aqi ?: 160
    val aqiStatus = weather?.aqiStatus ?: "Unhealthy"
    val uvVal = weather?.uvIndex ?: 7
    val uvStatus = weather?.uvStatus ?: "High"
    val precipChance = weather?.precipitationChance ?: 20
    val precipSummary = weather?.precipitationSummary ?: "20% chance at 6 PM"

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Temperature & Micro-Climate Global Metric Card (Featured Wide Bento)
        BentoCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_temperature"),
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
            borderColor = extraColors.bentoBorder,
            contentPadding = 22.dp,
            onClick = onTempClick
        ) {
            // Right-aligned 3D Animated Sun Sphere motion graphic with low-latency rendering
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 12.dp, y = (-4).dp)
            ) {
                AnimatedSunSphere(size = 142.dp)
            }

            Column(
                modifier = Modifier.fillMaxWidth(0.68f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MICRO-CLIMATE OVERVIEW",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp
                        ),
                        color = extraColors.bentoLavender
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$tempVal°",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 46.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (temperatureUnit == "°C") "C" else "F",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress indicator bar (Bento Grid signature style)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(extraColors.bentoBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.58f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoLavender)
                        )
                    }

                    Text(
                        text = "Feels like $feelsLikeVal$temperatureUnit • Optimal Sensor Stream",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        // 2. Air Quality & Ultra-Low Latency UV Index (Split 2 Column Bento)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // UV Index / Ultra-Low Latency Bento Card (Vibrant Lavender Bento)
            BentoCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag("card_uv_index"),
                backgroundColor = extraColors.bentoLavender,
                borderColor = Color.Transparent,
                contentPadding = 20.dp,
                onClick = onUvClick
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoDeepPurple.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "UV Index",
                                tint = extraColors.bentoDeepPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "UV $uvVal",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = extraColors.bentoDeepPurple
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "UV Index $uvStatus",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = extraColors.bentoDeepPurple,
                            lineHeight = 20.sp
                        )
                    )
                    Text(
                        text = "60 FPS Telemetry",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = extraColors.bentoDeepPurple.copy(alpha = 0.75f)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stylized dynamic vertical bars representing live sensor telemetry
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(12.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoDeepPurple.copy(alpha = 0.25f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(18.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoDeepPurple.copy(alpha = 0.45f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(26.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoDeepPurple.copy(alpha = 0.70f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoDeepPurple)
                        )
                    }
                }
            }

            // Air Quality & Sleek Sensor Bento Card (Deep Purple Surface Bento)
            BentoCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag("card_air_quality"),
                backgroundColor = extraColors.bentoDeepPurple,
                borderColor = extraColors.bentoBorder.copy(alpha = 0.3f),
                contentPadding = 20.dp,
                onClick = onAqiClick
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(extraColors.bentoLavender.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Air Quality",
                                tint = extraColors.bentoLavender,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "$aqiVal",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = extraColors.bentoLavender
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Air Quality",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "$aqiStatus Index",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = extraColors.bentoLavender.copy(alpha = 0.85f)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (aqiVal > 150) Color(0xFFFFB4AB) else extraColors.connectedGreen)
                        )
                        Text(
                            text = "Live Grounding",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = extraColors.bentoLavender
                            )
                        )
                    }
                }
            }
        }

        // 3. Precipitation & Transit Alert (Pastel Lilac Bento Card)
        BentoCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_precipitation"),
            backgroundColor = extraColors.bentoLilac,
            borderColor = Color.Transparent,
            contentPadding = 18.dp,
            onClick = onPrecipitationClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(extraColors.bentoDeepPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Precipitation",
                            tint = extraColors.bentoDeepPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "PRECIPITATION WINDOW",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            ),
                            color = extraColors.bentoDeepPurple.copy(alpha = 0.75f)
                        )
                        Text(
                            text = precipSummary,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = extraColors.bentoDeepPurple
                            )
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(extraColors.bentoDeepPurple)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable { onPrecipitationClick() }
                ) {
                    Text(
                        text = "Radar",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = extraColors.bentoLavender
                        )
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View Details",
                        tint = extraColors.bentoLavender,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
