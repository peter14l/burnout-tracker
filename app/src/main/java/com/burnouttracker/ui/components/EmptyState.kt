package com.burnouttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burnouttracker.ui.theme.*

/**
 * Empty state component with illustration and message
 * Used when screens have no data yet
 */
@Composable
fun EmptyState(
    emoji: String,
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Illustration circle with gradient
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Pre-defined empty states for different screens
 */
object EmptyStates {
    @Composable
    fun NoCheckIns() = EmptyState(
        emoji = "📊",
        title = "No check-ins yet",
        message = "Start tracking your financial stress to see patterns and insights"
    )

    @Composable
    fun NoExpenses() = EmptyState(
        emoji = "💰",
        title = "No expenses logged",
        message = "Track your spending to understand how it relates to your stress levels"
    )

    @Composable
    fun NoJournalEntries() = EmptyState(
        emoji = "📝",
        title = "Journal is empty",
        message = "Your thoughts and moods will appear here as you check in daily"
    )

    @Composable
    fun NoInsights() = EmptyState(
        emoji = "🔍",
        title = "Insights coming soon",
        message = "Check back after a few days of tracking to see your patterns"
    )

    @Composable
    fun NoRecoveryPlans() = EmptyState(
        emoji = "🌱",
        title = "No recovery plans",
        message = "Start a wellness plan to build healthy financial habits"
    )
}
