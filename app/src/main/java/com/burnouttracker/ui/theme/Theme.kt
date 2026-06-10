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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = BurnoutColors.SecondaryLight,
    tertiary = BurnoutColors.TertiaryContainer,
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2D2D2D),
    surfaceVariant = Color(0xFF3D3D3D),
    onPrimary = BurnoutColors.TextPrimary,
    onSecondary = BurnoutColors.TextPrimary,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFFB0B0B0),
    error = BurnoutColors.Error
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = BurnoutColors.Secondary,
    tertiary = BurnoutColors.Tertiary,
    background = BurnoutColors.Background,
    surface = BurnoutColors.Surface,
    surfaceVariant = BurnoutColors.SurfaceVariant,
    onPrimary = BurnoutColors.TextOnPrimary,
    onSecondary = BurnoutColors.TextOnPrimary,
    onBackground = BurnoutColors.TextPrimary,
    onSurface = BurnoutColors.TextPrimary,
    onSurfaceVariant = BurnoutColors.TextSecondary,
    error = BurnoutColors.Error,
    primaryContainer = BurnoutColors.PrimaryContainer,
    onPrimaryContainer = BurnoutColors.OnPrimaryContainer,
    secondaryContainer = BurnoutColors.SecondaryContainer,
    onSecondaryContainer = BurnoutColors.OnSecondaryContainer
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
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
