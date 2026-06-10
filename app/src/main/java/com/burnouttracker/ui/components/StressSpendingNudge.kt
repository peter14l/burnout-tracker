package com.burnouttracker.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burnouttracker.ui.theme.*

@Composable
fun StressSpendingNudge(
    stressLevel: Int,
    onDismiss: () -> Unit,
    onContinueToApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nudgeMessage = when {
        stressLevel <= 3 -> "You're feeling calm right now. Good time to stick to your budget."
        stressLevel <= 5 -> "Moderate stress detected. Take a breath before any purchases."
        stressLevel <= 7 -> "High stress! You're more likely to impulse buy right now."
        else -> "Critical stress. This is when comfort spending happens most."
    }
    
    val actionSuggestion = when {
        stressLevel <= 3 -> "Continue with your planned spending"
        stressLevel <= 5 -> "Try the 30-second breathing exercise first"
        stressLevel <= 7 -> "Wait 10 minutes before any non-essential purchase"
        else -> "Open the recovery tab for immediate stress relief"
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = getStressColor(stressLevel).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = getStressColor(stressLevel).copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp),
                        tint = getStressColor(stressLevel)
                    )
                }
                
                Column {
                    Text(
                        text = "Stress-Spending Alert",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Stress level: $stressLevel/10",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Message
            Text(
                text = nudgeMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action suggestion
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = WarmSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = actionSuggestion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("I'm aware")
                }
                
                Button(
                    onClick = onContinueToApp,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getStressColor(stressLevel)
                    )
                ) {
                    Text(
                        text = if (stressLevel > 5) "Get help" else "Continue",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun InlineStressNudge(
    stressLevel: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (stressLevel > 5) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            color = getStressColor(stressLevel).copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = getStressColor(stressLevel),
                    modifier = Modifier.size(16.dp)
                )
                
                Text(
                    text = "High stress may trigger impulse spending",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
