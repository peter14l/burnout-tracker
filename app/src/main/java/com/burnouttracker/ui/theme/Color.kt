package com.burnouttracker.ui.theme

import androidx.compose.ui.graphics.Color

// Primary - Calming purple/lavender
val Purple80 = Color(0xFFB8A9FF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF7C5CFC)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)

// Burnout Tracker Custom Colors
object BurnoutColors {
    // Primary - Calming purple/lavender
    val Primary = Color(0xFF7C5CFC)
    val PrimaryLight = Color(0xFFB8A9FF)
    val PrimaryDark = Color(0xFF5A3FD9)
    val PrimaryContainer = Color(0xFFE8DEFF)
    val OnPrimaryContainer = Color(0xFF21005E)

    // Secondary - Warm coral (for positive actions)
    val Secondary = Color(0xFFFF6B6B)
    val SecondaryLight = Color(0xFFFF9E9E)
    val SecondaryDark = Color(0xFFD94444)
    val SecondaryContainer = Color(0xFFFFDAD6)
    val OnSecondaryContainer = Color(0xFF410001)

    // Tertiary - Teal (for calm states)
    val Tertiary = Color(0xFF00BCD4)
    val TertiaryContainer = Color(0xFFB2EBF2)

    // Background - Soft white/cream
    val Background = Color(0xFFFAFAFA)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF5F5F5)

    // Stress Colors (for visualization)
    val StressLow = Color(0xFF4CAF50)       // Green
    val StressModerate = Color(0xFFFFC107)   // Yellow
    val StressHigh = Color(0xFFFF9800)       // Orange
    val StressCritical = Color(0xFFF44336)   // Red

    // Mood Colors
    val MoodAnxious = Color(0xFF9C27B0)
    val MoodStressed = Color(0xFFFF5722)
    val MoodOverwhelmed = Color(0xFF795548)
    val MoodNeutral = Color(0xFF607D8B)
    val MoodCalm = Color(0xFF00BCD4)
    val MoodHopeful = Color(0xFF8BC34A)
    val MoodRelieved = Color(0xFF3F51B5)

    // Status Colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Error = Color(0xFFF44336)
    val Info = Color(0xFF2196F3)

    // Text Colors
    val TextPrimary = Color(0xFF1A1A1A)
    val TextSecondary = Color(0xFF666666)
    val TextTertiary = Color(0xFF999999)
    val TextOnPrimary = Color(0xFFFFFFFF)
}

fun getStressColor(score: Int): Color {
    return when {
        score <= 3 -> BurnoutColors.StressLow
        score <= 5 -> BurnoutColors.StressModerate
        score <= 7 -> BurnoutColors.StressHigh
        else -> BurnoutColors.StressCritical
    }
}

fun getStressLabel(score: Int): String {
    return when {
        score <= 3 -> "Low"
        score <= 5 -> "Moderate"
        score <= 7 -> "High"
        else -> "Critical"
    }
}
