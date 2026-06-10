package com.burnouttracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val WarmLightScheme = lightColorScheme(
    primary = WarmPrimary,
    onPrimary = TextOnPrimary,
    primaryContainer = WarmPrimaryLight,
    onPrimaryContainer = Color(0xFF3E2723),
    secondary = WarmSecondary,
    onSecondary = TextOnPrimary,
    secondaryContainer = WarmSecondaryLight,
    onSecondaryContainer = Color(0xFF1B5E20),
    tertiary = WarmTertiary,
    onTertiary = TextOnPrimary,
    tertiaryContainer = WarmTertiaryLight,
    onTertiaryContainer = Color(0xFF4A148C),
    background = WarmBackground,
    onBackground = TextPrimary,
    surface = WarmSurface,
    onSurface = TextPrimary,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = Color(0xFFEF5350),
    onError = TextOnPrimary
)

private val WarmDarkScheme = darkColorScheme(
    primary = WarmPrimaryLight,
    onPrimary = Color(0xFF3E2723),
    primaryContainer = WarmPrimary,
    onPrimaryContainer = WarmPrimaryLight,
    secondary = WarmSecondaryLight,
    onSecondary = Color(0xFF1B5E20),
    secondaryContainer = WarmSecondary,
    onSecondaryContainer = WarmSecondaryLight,
    tertiary = WarmTertiaryLight,
    onTertiary = Color(0xFF4A148C),
    tertiaryContainer = WarmTertiary,
    onTertiaryContainer = WarmTertiaryLight,
    background = Color(0xFF1A1A1A),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF2D2D2D),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF3D3D3D),
    onSurfaceVariant = Color(0xFFB0B0B0),
    error = Color(0xFFEF9A9A),
    onError = Color(0xFF1A1A1A)
)

@Composable
fun BurnoutTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> WarmDarkScheme
        else -> WarmLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
