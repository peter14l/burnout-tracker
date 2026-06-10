package com.burnouttracker.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burnouttracker.domain.model.*
import com.burnouttracker.ui.theme.*

@Composable
fun CycleBreakerStatsCard(
    stats: CycleBreakerStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = WarmPrimary
                )
                Text(
                    text = "Cycle Breaker Stats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MiniStatItem(
                    value = "${stats.cyclesBroken}",
                    label = "Cycles Broken",
                    icon = Icons.Default.BreakfastDining
                )
                MiniStatItem(
                    value = "${stats.currentStreak}",
                    label = "Day Streak",
                    icon = Icons.Default.LocalFireDepartment
                )
                MiniStatItem(
                    value = "$${String.format("%.0f", stats.totalSaved)}",
                    label = "Total Saved",
                    icon = Icons.Default.Savings
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MiniStatItem(
                    value = "${stats.triggersCaught}",
                    label = "Triggers Caught",
                    icon = Icons.Default.Flag
                )
                MiniStatItem(
                    value = "${stats.recoveryActions}",
                    label = "Recovery Actions",
                    icon = Icons.Default.SelfImprovement
                )
                MiniStatItem(
                    value = "${stats.longestStreak}",
                    label = "Best Streak",
                    icon = Icons.Default.EmojiEvents
                )
            }
        }
    }
}

@Composable
fun MiniStatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AchievementCarousel(
    achievements: List<CycleBreakerAchievement>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Achievements",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${achievements.count { it.isUnlocked }}/${achievements.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                AchievementBadge(achievement = achievement)
            }
        }
    }
}

@Composable
fun AchievementBadge(
    achievement: CycleBreakerAchievement,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (achievement.isUnlocked) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "achievement_scale"
    )
    
    Card(
        modifier = modifier
            .width(120.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Text(
                text = achievement.icon,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.then(
                    if (!achievement.isUnlocked) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier
                    }
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = achievement.title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = if (achievement.isUnlocked) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            if (achievement.isUnlocked) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement.reward.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getRequirementText(achievement.requirement),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun getRequirementText(requirement: CycleBreakRequirement): String {
    return when (requirement) {
        is CycleBreakRequirement.CyclesBroken -> "Break ${requirement.count} cycles"
        is CycleBreakRequirement.ConsecutiveDays -> "${requirement.days} day streak"
        is CycleBreakRequirement.MoneySaved -> "Save $${String.format("%.0f", requirement.amount)}"
        is CycleBreakRequirement.TriggersCaught -> "Catch ${requirement.count} triggers"
        is CycleBreakRequirement.RecoveryActions -> "${requirement.count} recovery actions"
        is CycleBreakRequirement.Combined -> "Complete all challenges"
    }
}

@Composable
fun AchievementUnlockPopup(
    achievement: CycleBreakerAchievement,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "popup_scale"
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.scale(scale),
        icon = {
            Text(
                text = achievement.icon,
                style = MaterialTheme.typography.displayLarge
            )
        },
        title = {
            Text(
                text = "Achievement Unlocked!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = achievement.reward.description,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Awesome!")
            }
        }
    )
}
