package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TimelineHourEntity
import com.example.ui.theme.LocalExtraColors

@Composable
fun MicroClimateTimeline(
    timeline: List<TimelineHourEntity>,
    onViewAllClick: () -> Unit,
    onHourClick: (TimelineHourEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val extraColors = LocalExtraColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Micro-Climate Timeline",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "View All",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .padding(4.dp)
                    .testTag("btn_view_all_timeline")
            )
        }

        // Horizontal Snap Carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("timeline_lazy_row")
        ) {
            items(timeline, key = { it.id }) { hour ->
                val isAlert = hour.isAlert
                val cardBg = if (isAlert) extraColors.skyLight else MaterialTheme.colorScheme.surfaceContainerLowest
                val cardBorder = if (isAlert) MaterialTheme.colorScheme.primary.copy(alpha = 0.35f) else extraColors.surfaceBorder

                BentoCard(
                    modifier = Modifier
                        .width(204.dp)
                        .height(130.dp)
                        .testTag("timeline_card_${hour.hourOfDay}"),
                    backgroundColor = cardBg,
                    borderColor = cardBorder,
                    contentPadding = 14.dp,
                    onClick = { onHourClick(hour) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Header (Badge + Condition Icon)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (hour.isPrimaryPill) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = hour.timeLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            } else {
                                Text(
                                    text = hour.timeLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isAlert) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isAlert) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }

                            val icon = getConditionIcon(hour.conditionTitle)
                            Icon(
                                imageVector = icon,
                                contentDescription = hour.conditionTitle,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Bottom Title + Subtitle
                        Column {
                            Text(
                                text = hour.conditionTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = hour.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isAlert) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isAlert) FontWeight.Medium else FontWeight.Normal
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getConditionIcon(condition: String): ImageVector {
    val lower = condition.lowercase()
    return when {
        lower.contains("partly") -> Icons.Default.WbCloudy
        lower.contains("clear") && lower.contains("night") -> Icons.Default.Bedtime
        lower.contains("clear") || lower.contains("sun") -> Icons.Default.WbSunny
        lower.contains("rain") || lower.contains("drizzle") -> Icons.Default.WaterDrop
        lower.contains("thunder") || lower.contains("storm") -> Icons.Default.Thunderstorm
        lower.contains("star") || lower.contains("calm") -> Icons.Default.Bedtime
        else -> Icons.Default.Cloud
    }
}
