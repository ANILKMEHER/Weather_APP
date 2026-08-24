package com.example.ui.components.motion

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

enum class WeatherAnimType {
    NONE, DRIZZLE, SUNNY_SPECS, CLOUDY_MIST
}

private data class Particle(
    val startXRatio: Float,
    val speed: Float,
    val length: Float,
    val alpha: Float
)

@Composable
fun WeatherBackgroundEffect(
    type: WeatherAnimType = WeatherAnimType.DRIZZLE,
    modifier: Modifier = Modifier
) {
    if (type == WeatherAnimType.NONE) return

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    val particles = remember(type) {
        List(28) {
            Particle(
                startXRatio = Random.nextFloat(),
                speed = Random.nextFloat() * 0.6f + 0.7f,
                length = Random.nextFloat() * 20f + 15f,
                alpha = Random.nextFloat() * 0.35f + 0.15f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        when (type) {
            WeatherAnimType.DRIZZLE -> {
                particles.forEachIndexed { index, p ->
                    val y = ((animProgress * p.speed + (index * 0.05f)) % 1.0f) * (height + 50f) - 20f
                    val x = (p.startXRatio * width + (y * 0.15f)) % width
                    drawLine(
                        color = Color(0xFF60A5FA).copy(alpha = p.alpha),
                        start = Offset(x, y),
                        end = Offset(x + 4f, y + p.length),
                        strokeWidth = 2.5f
                    )
                }
            }
            WeatherAnimType.SUNNY_SPECS -> {
                particles.take(14).forEachIndexed { index, p ->
                    val y = height - (((animProgress * p.speed + (index * 0.07f)) % 1.0f) * height)
                    val x = p.startXRatio * width
                    drawCircle(
                        color = Color(0xFFFFD54F).copy(alpha = p.alpha * 0.6f),
                        radius = 3.5f,
                        center = Offset(x, y)
                    )
                }
            }
            WeatherAnimType.CLOUDY_MIST -> {
                // Subtle drifting mist
                particles.take(8).forEachIndexed { index, p ->
                    val x = ((animProgress * 0.3f * p.speed + (index * 0.12f)) % 1.0f) * width
                    val y = (index * 0.12f) * height + 40f
                    drawCircle(
                        color = Color(0x22FFFFFF),
                        radius = 45f + (index * 8f),
                        center = Offset(x, y)
                    )
                }
            }
            WeatherAnimType.NONE -> {}
        }
    }
}
