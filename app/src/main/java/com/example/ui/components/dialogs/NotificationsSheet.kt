package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BentoCard
import com.example.ui.theme.LocalExtraColors

data class WeatherNotification(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val type: String // "ALERT", "SYNC", "AQI", "PRECIPITATION"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
    onDismiss: () -> Unit
) {
    val extraColors = LocalExtraColors.current
    val notifications = listOf(
        WeatherNotification(
            id = "1",
            title = "Transit Precipitation Window",
            description = "Rain protection advised for Pimple Saudagar between 6:00 PM - 7:30 PM (75% probability).",
            timeAgo = "10m ago",
            type = "PRECIPITATION"
        ),
        WeatherNotification(
            id = "2",
            title = "AQI Advisory Alert",
            description = "Air Quality Index reached 160 (Unhealthy) in your current zone. Limit strenuous outdoor workouts.",
            timeAgo = "45m ago",
            type = "AQI"
        ),
        WeatherNotification(
            id = "3",
            title = "Cloud Synchronization Verified",
            description = "Snapshot replicated with Pixel-ClimatoSync across 4 zones.",
            timeAgo = "2h ago",
            type = "SYNC"
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Micro-Climate Alerts",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .testTag("notifications_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications.size) { index ->
                    val item = notifications[index]
                    val isWarning = item.type == "AQI" || item.type == "PRECIPITATION"

                    BentoCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = if (isWarning) extraColors.skyLight else MaterialTheme.colorScheme.surfaceContainerLow,
                        borderColor = if (isWarning) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else extraColors.surfaceBorder,
                        contentPadding = 14.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (item.type) {
                                            "PRECIPITATION" -> MaterialTheme.colorScheme.primaryContainer
                                            "AQI" -> MaterialTheme.colorScheme.errorContainer
                                            else -> MaterialTheme.colorScheme.secondaryContainer
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                val icon = when (item.type) {
                                    "PRECIPITATION" -> Icons.Default.WaterDrop
                                    "AQI" -> Icons.Default.Air
                                    else -> Icons.Default.CloudSync
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.type,
                                    tint = when (item.type) {
                                        "PRECIPITATION" -> MaterialTheme.colorScheme.onPrimaryContainer
                                        "AQI" -> MaterialTheme.colorScheme.onErrorContainer
                                        else -> MaterialTheme.colorScheme.onSecondaryContainer
                                    },
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = item.timeAgo,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
