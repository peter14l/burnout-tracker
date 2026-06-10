package com.burnouttracker.ui.theme

import androidx.compose.ui.graphics.Color

// Warm & Supportive Palette with Minimalist Blend
// Primary - Warm peach/coral (supportive, not alarming)
val WarmPrimary = Color(0xFFFF8A65)       // Soft coral
val WarmPrimaryLight = Color(0xFFFFCCBC)  // Light peach
val WarmPrimaryDark = Color(0xFFE64A19)   // Deep coral

// Secondary - Calming sage green (growth, recovery)
val WarmSecondary = Color(0xFF81C784)     // Sage green
val WarmSecondaryLight = Color(0xFFC8E6C9)
val WarmSecondaryDark = Color(0xFF388E3C)

// Tertiary - Soft lavender (calm, mindfulness)
val WarmTertiary = Color(0xFFCE93D8)      // Soft purple
val WarmTertiaryLight = Color(0xFFF3E5F5)

// Background - Warm off-white (minimalist, clean)
val WarmBackground = Color(0xFFFEFCF9)    // Warm white
val WarmSurface = Color(0xFFFFFFFF)
val WarmSurfaceVariant = Color(0xFFF8F5F2) // Warm gray

// Stress Colors (warmer, less clinical)
val StressLow = Color(0xFF81C784)         // Sage green
val StressModerate = Color(0xFFFFD54F)    // Warm yellow
val StressHigh = Color(0xFFFFAB91)        // Soft coral
val StressCritical = Color(0xFFEF9A9A)    // Muted red

// Text Colors (softer, more readable)
val TextPrimary = Color(0xFF2D2D2D)       // Soft black
val TextSecondary = Color(0xFF757575)     // Warm gray
val TextTertiary = Color(0xFFBDBDBD)      // Light gray
val TextOnPrimary = Color(0xFFFFFFFF)

// Accent Colors for financial data
val SpendingGreen = Color(0xFF66BB6A)     // Under budget
val SpendingYellow = Color(0xFFFFCA28)    // Near limit
val SpendingRed = Color(0xFFEF5350)       // Over budget

// Gradient colors for cards
val GradientStart = Color(0xFFFFF3E0)     // Warm peach
val GradientEnd = Color(0xFFE8F5E9)       // Soft green

fun getStressColor(score: Int): Color {
    return when {
        score <= 3 -> StressLow
        score <= 5 -> StressModerate
        score <= 7 -> StressHigh
        else -> StressCritical
    }
}

fun getStressLabel(score: Int): String {
    return when {
        score <= 3 -> "Calm"
        score <= 5 -> "Moderate"
        score <= 7 -> "Elevated"
        else -> "High"
    }
}
