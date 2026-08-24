package com.example.ui.components.motion

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3D-styled animated Weather Sun Sphere with orbiting capsule rays,
 * smooth ambient glow breathing, and multi-tone atmospheric depth.
 */
@Composable
fun AnimatedSunSphere(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    isSunny: Boolean = true,
    isNight: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sunSphereAnim")

    // Slow rotation of outer rays
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Breathing pulse for core glow
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // Breathing pulse for ray opacity & scale
    val rayPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rayPulse"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = this.size.minDimension / 2f
            val coreRadius = radius * 0.44f

            if (isNight) {
                // Moon aesthetic for night micro-climate
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x5594BAF7),
                            Color(0x2260A5FA),
                            Color.Transparent
                        ),
                        center = center,
                        radius = coreRadius * 2.2f * glowPulse
                    ),
                    radius = coreRadius * 2.2f * glowPulse,
                    center = center
                )

                // Moon sphere
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE2E8F0),
                            Color(0xFF93C5FD),
                            Color(0xFF3B82F6)
                        ),
                        start = Offset(center.x - coreRadius, center.y - coreRadius),
                        end = Offset(center.x + coreRadius, center.y + coreRadius)
                    ),
                    radius = coreRadius,
                    center = center
                )
                return@Canvas
            }

            // 1. Atmospheric Ambient Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x66B0D5FF),
                        Color(0x3374B0FF),
                        Color(0x10A0C4FF),
                        Color.Transparent
                    ),
                    center = center,
                    radius = coreRadius * 2.6f * glowPulse
                ),
                radius = coreRadius * 2.6f * glowPulse,
                center = center
            )

            // 2. Orbital 12 Capsule Rays
            val rayCount = 12
            val rayDistance = radius * 0.72f
            val rayWidth = radius * 0.12f
            val rayHeight = radius * 0.28f

            rotate(degrees = rotationAngle, pivot = center) {
                for (i in 0 until rayCount) {
                    val angleDeg = (i * 360f / rayCount)
                    val angleRad = angleDeg * (PI / 180f).toFloat()

                    val rayCenter = Offset(
                        x = center.x + rayDistance * cos(angleRad),
                        y = center.y + rayDistance * sin(angleRad)
                    )

                    // Alternate ray brightness
                    val rayAlpha = if (i % 2 == 0) rayPulse else (1.7f - rayPulse)
                    val baseRayColor = when (i % 4) {
                        0 -> Color(0xFFD6EBFF)
                        1 -> Color(0xFFB3DCFF)
                        2 -> Color(0xFF93CBFF)
                        else -> Color(0xFF6BB5FF)
                    }

                    rotate(degrees = angleDeg + 90f, pivot = rayCenter) {
                        val left = rayCenter.x - rayWidth / 2f
                        val top = rayCenter.y - rayHeight / 2f

                        // Ray shadow/glow
                        drawRoundRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    baseRayColor.copy(alpha = 0.5f * rayAlpha),
                                    Color.Transparent
                                ),
                                center = rayCenter,
                                radius = rayHeight * 0.9f
                            ),
                            topLeft = Offset(left - rayWidth * 0.3f, top - rayHeight * 0.2f),
                            size = Size(rayWidth * 1.6f, rayHeight * 1.4f),
                            cornerRadius = CornerRadius(rayWidth * 0.8f, rayWidth * 0.8f)
                        )

                        // Pill Ray Shape
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.95f * rayAlpha),
                                    baseRayColor.copy(alpha = 0.85f * rayAlpha)
                                ),
                                startY = top,
                                endY = top + rayHeight
                            ),
                            topLeft = Offset(left, top),
                            size = Size(rayWidth, rayHeight),
                            cornerRadius = CornerRadius(rayWidth / 2f, rayWidth / 2f)
                        )
                    }
                }
            }

            // 3. Central 3D Sun Sphere
            // Underlying shadow for floating 3D feel
            drawCircle(
                color = Color(0x20003B82),
                radius = coreRadius * 1.05f,
                center = Offset(center.x + 2f, center.y + 4f)
            )

            // Inner sphere with modern 3D lighting gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),       // Shiny top-left specular highlight
                        Color(0xFFEFF6FF),
                        Color(0xFFBFDBFE),       // Soft sky blue mid-tone
                        Color(0xFF60A5FA),       // Saturated blue shading
                        Color(0xFF2563EB)        // Atmospheric rim depth
                    ),
                    center = Offset(center.x - coreRadius * 0.35f, center.y - coreRadius * 0.35f),
                    radius = coreRadius * 1.35f
                ),
                radius = coreRadius,
                center = center
            )

            // Soft specular glass gleam
            val gleamPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = center.x - coreRadius * 0.65f,
                        top = center.y - coreRadius * 0.75f,
                        right = center.x + coreRadius * 0.1f,
                        bottom = center.y - coreRadius * 0.2f,
                        cornerRadius = CornerRadius(coreRadius * 0.4f, coreRadius * 0.4f)
                    )
                )
            }
            drawPath(
                path = gleamPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.75f),
                        Color.White.copy(alpha = 0.1f)
                    )
                )
            )
        }
    }
}
