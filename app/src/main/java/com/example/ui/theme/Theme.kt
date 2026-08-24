package com.example.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BentoPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = BentoPrimaryContainerLight,
    onPrimaryContainer = BentoOnPrimaryContainerLight,
    secondary = BentoTextSecondaryLight,
    onSecondary = Color.White,
    secondaryContainer = BentoPillActive,
    onSecondaryContainer = BentoDeepPurple,
    tertiary = BentoDeepPurple,
    onTertiary = Color.White,
    tertiaryContainer = BentoPastelLilac,
    onTertiaryContainer = BentoDeepViolet,
    background = BentoBackgroundLight,
    onBackground = BentoTextPrimaryLight,
    surface = BentoSurfaceLight,
    onSurface = BentoTextPrimaryLight,
    surfaceVariant = BentoSurfaceContainerLight,
    onSurfaceVariant = BentoTextSecondaryLight,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = BentoSurfaceContainerLight,
    surfaceContainer = BentoSurfaceContainerLight,
    surfaceContainerHigh = BentoPillActive,
    surfaceContainerHighest = BentoPastelLilac,
    outline = BentoBorderLight,
    outlineVariant = BentoBorderLight,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainerRed,
    onErrorContainer = Color(0xFF93000A)
)

private val DarkColorScheme = darkColorScheme(
    primary = BentoLavender,
    onPrimary = BentoDeepPurple,
    primaryContainer = BentoDeepPurple,
    onPrimaryContainer = BentoLavender,
    secondary = BentoTextSecondaryDark,
    onSecondary = BentoBackgroundDark,
    secondaryContainer = BentoSurfaceDark,
    onSecondaryContainer = BentoTextPrimaryDark,
    tertiary = BentoPastelLilac,
    onTertiary = BentoDeepViolet,
    tertiaryContainer = BentoDeepPurple,
    onTertiaryContainer = BentoPastelLilac,
    background = BentoBackgroundDark,
    onBackground = BentoTextPrimaryDark,
    surface = BentoSurfaceDark,
    onSurface = BentoTextPrimaryDark,
    surfaceVariant = BentoSurfaceDark,
    onSurfaceVariant = BentoTextSecondaryDark,
    surfaceContainerLowest = BentoBackgroundDark,
    surfaceContainerLow = BentoSurfaceDark,
    surfaceContainer = BentoSurfaceDark,
    surfaceContainerHigh = Color(0xFF36343B),
    surfaceContainerHighest = Color(0xFF49454F),
    outline = BentoBorderDark,
    outlineVariant = BentoBorderDarkSubtle,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

data class ExtraColors(
    val skyLight: Color,
    val surfaceBorder: Color,
    val warning: Color,
    val success: Color,
    val info: Color,
    val bentoLavender: Color = BentoLavender,
    val bentoDeepPurple: Color = BentoDeepPurple,
    val bentoLilac: Color = BentoPastelLilac,
    val bentoCard: Color = BentoSurfaceDark,
    val bentoBorder: Color = BentoBorderDark,
    val bentoPillActive: Color = BentoPillActive,
    val connectedGreen: Color = BentoGreenConnected
)

val LocalExtraColors = staticCompositionLocalOf {
    ExtraColors(
        skyLight = BentoPastelLilac,
        surfaceBorder = BentoBorderLight,
        warning = WarningAmber,
        success = SuccessGreen,
        info = InfoCyan,
        bentoLavender = BentoLavender,
        bentoDeepPurple = BentoDeepPurple,
        bentoLilac = BentoPastelLilac,
        bentoCard = BentoSurfaceLight,
        bentoBorder = BentoBorderLight,
        bentoPillActive = BentoPillActive,
        connectedGreen = BentoGreenConnected
    )
}

@Composable
fun ClimatoIQTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val baseScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Smooth low-latency color transitions when toggling dark mode
    val animatedPrimary = animateColorAsState(baseScheme.primary, tween(250), label = "primary").value
    val animatedBackground = animateColorAsState(baseScheme.background, tween(250), label = "bg").value
    val animatedSurface = animateColorAsState(baseScheme.surface, tween(250), label = "surface").value
    val animatedOnSurface = animateColorAsState(baseScheme.onSurface, tween(250), label = "onSurface").value

    val animatedColorScheme = baseScheme.copy(
        primary = animatedPrimary,
        background = animatedBackground,
        surface = animatedSurface,
        onSurface = animatedOnSurface
    )

    val extraColors = if (darkTheme) {
        ExtraColors(
            skyLight = BentoDeepPurple.copy(alpha = 0.4f),
            surfaceBorder = BentoBorderDark,
            warning = Color(0xFFFBBF24),
            success = BentoGreenConnected,
            info = Color(0xFF38BDF8),
            bentoLavender = BentoLavender,
            bentoDeepPurple = BentoDeepPurple,
            bentoLilac = BentoPastelLilac,
            bentoCard = BentoSurfaceDark,
            bentoBorder = BentoBorderDark,
            bentoPillActive = BentoPillActive,
            connectedGreen = BentoGreenConnected
        )
    } else {
        ExtraColors(
            skyLight = BentoPastelLilac.copy(alpha = 0.5f),
            surfaceBorder = BentoBorderLight,
            warning = WarningAmber,
            success = SuccessGreen,
            info = InfoCyan,
            bentoLavender = BentoLavender,
            bentoDeepPurple = BentoDeepPurple,
            bentoLilac = BentoPastelLilac,
            bentoCard = BentoSurfaceLight,
            bentoBorder = BentoBorderLight,
            bentoPillActive = BentoPillActive,
            connectedGreen = BentoGreenConnected
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = animatedBackground.toArgb()
            window.navigationBarColor = animatedBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = animatedColorScheme,
            typography = Typography,
            content = content
        )
    }
}
