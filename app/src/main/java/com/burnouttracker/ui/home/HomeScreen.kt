package com.burnouttracker.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.burnouttracker.ui.components.StressAvatar
import com.burnouttracker.ui.components.AvatarSize
import com.burnouttracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCheckIn: () -> Unit,
    onViewInsights: () -> Unit,
    onViewRecovery: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.hasCheckedInToday) "Welcome back" else "Good morning",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Light
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero: Avatar + Score
            item {
                AvatarHeroCard(
                    stressLevel = uiState.latestStress,
                    hasCheckedIn = uiState.hasCheckedInToday,
                    onCheckIn = onCheckIn
                )
            }

            // Financial Wellness Score
            item {
                WellnessScoreCard(
                    stressScore = uiState.latestStress,
                    streakDays = uiState.streakDays
                )
            }

            // Spending ↔ Stress Correlation
            item {
                CorrelationCard(
                    stressLevel = uiState.latestStress,
                    totalSpending7Days = uiState.totalSpending7Days,
                    spendingTrend = uiState.spendingTrend,
                    recentExpenses = uiState.recentExpenses
                )
            }

            // Quick Check-in CTA
            if (!uiState.hasCheckedInToday) {
                item {
                    CheckInCTA(onCheckIn = onCheckIn)
                }
            }

            // Quick Actions
            item {
                QuickActionsSection(
                    onViewInsights = onViewInsights,
                    onViewRecovery = onViewRecovery
                )
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun AvatarHeroCard(
    stressLevel: Int,
    hasCheckedIn: Boolean,
    onCheckIn: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StressAvatar(
                    stressLevel = stressLevel,
                    size = AvatarSize.XLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (stressLevel == 0) "How are you feeling?" else getStressMessage(stressLevel),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Normal
                )

                if (stressLevel > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Stress level: $stressLevel/10",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

fun getStressMessage(level: Int): String {
    return when {
        level <= 2 -> "You're doing great today!"
        level <= 4 -> "Things are looking good"
        level <= 6 -> "Taking it one step at a time"
        level <= 8 -> "Let's work through this together"
        else -> "We're here for you"
    }
}

@Composable
fun WellnessScoreCard(
    stressScore: Int,
    streakDays: Int
) {
    // Simple wellness score: inverse of stress + streak bonus
    val wellnessScore = ((10 - stressScore) * 10 + (streakDays * 5)).coerceIn(0, 100)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Financial Wellness",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$wellnessScore%",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = getWellnessColor(wellnessScore)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { wellnessScore / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = getWellnessColor(wellnessScore),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Streak", value = "${streakDays}d", icon = Icons.Default.LocalFireDepartment)
                StatItem(label = "Stress", value = "$stressScore/10", icon = Icons.Default.MonitorHeart)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun getWellnessColor(score: Int): Color {
    return when {
        score >= 70 -> StressLow
        score >= 40 -> StressModerate
        else -> StressHigh
    }
}

@Composable
fun CorrelationCard(
    stressLevel: Int,
    totalSpending7Days: Double,
    spendingTrend: Double,
    recentExpenses: List<com.burnouttracker.domain.model.Expense>
) {
    val spendingLevel = when {
        stressLevel <= 3 -> "Low"
        stressLevel <= 6 -> "Moderate"
        else -> "High"
    }

    val correlationMessage = when {
        totalSpending7Days == 0.0 -> "Start logging expenses to see your spending pattern"
        stressLevel <= 3 && totalSpending7Days < 500 -> "You're managing stress and spending well"
        stressLevel <= 6 -> "Moderate stress may trigger impulse buys"
        else -> "High stress often leads to comfort spending"
    }

    val trendText = when {
        spendingTrend > 5 -> "↑ ${String.format("%.0f", spendingTrend)}% vs last week"
        spendingTrend < -5 -> "↓ ${String.format("%.0f", kotlin.math.abs(spendingTrend))}% vs last week"
        else -> "Stable this week"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Spending Pattern",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Stress",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$stressLevel/10",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = getStressColor(stressLevel)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "7-Day Spending",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.0f", totalSpending7Days)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            totalSpending7Days > 1000 -> SpendingRed
                            totalSpending7Days > 500 -> SpendingYellow
                            else -> SpendingGreen
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = trendText,
                style = MaterialTheme.typography.labelSmall,
                color = when {
                    spendingTrend > 5 -> SpendingRed
                    spendingTrend < -5 -> SpendingGreen
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = correlationMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CheckInCTA(onCheckIn: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckIn() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Daily Check-in",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Track how you're feeling today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go to check-in",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    onViewInsights: () -> Unit,
    onViewRecovery: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Insights,
            title = "Insights",
            subtitle = "Trends & patterns",
            onClick = onViewInsights
        )
        ActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.SelfImprovement,
            title = "Recovery",
            subtitle = "Wellness plans",
            onClick = onViewRecovery
        )
    }
}

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
