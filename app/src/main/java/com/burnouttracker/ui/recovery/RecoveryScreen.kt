package com.burnouttracker.ui.recovery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class RecoveryPlanItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val difficulty: String,
    val estimatedTime: Int,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreen(
    onBack: () -> Unit
) {
    val recoveryPlans = listOf(
        RecoveryPlanItem(
            id = "1",
            title = "Breathing Reset",
            description = "3-minute guided breathing exercise",
            icon = Icons.Default.SelfImprovement,
            difficulty = "Easy",
            estimatedTime = 3,
            category = "Breathing"
        ),
        RecoveryPlanItem(
            id = "2",
            title = "No-Spend Day",
            description = "Challenge to avoid non-essential spending",
            icon = Icons.Outlined.MoneyOff,
            difficulty = "Medium",
            estimatedTime = 0,
            category = "Financial"
        ),
        RecoveryPlanItem(
            id = "3",
            title = "Gratitude Log",
            description = "Write 3 things money can't buy",
            icon = Icons.Outlined.Favorite,
            difficulty = "Easy",
            estimatedTime = 5,
            category = "Mindfulness"
        ),
        RecoveryPlanItem(
            id = "4",
            title = "Bill Face-Off",
            description = "Open one bill you've been avoiding",
            icon = Icons.Outlined.Receipt,
            difficulty = "Hard",
            estimatedTime = 10,
            category = "Financial"
        ),
        RecoveryPlanItem(
            id = "5",
            title = "Support Reach",
            description = "Text one person about how you're feeling",
            icon = Icons.Outlined.People,
            difficulty = "Medium",
            estimatedTime = 5,
            category = "Social"
        )
    )

    val categories = listOf("All", "Breathing", "Financial", "Mindfulness", "Social")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredPlans = if (selectedCategory == "All") {
        recoveryPlans
    } else {
        recoveryPlans.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recovery Plans") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Category filters
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) }
                        )
                    }
                }
            }

            // Recovery plans
            items(filteredPlans) { plan ->
                RecoveryPlanCard(plan = plan)
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun RecoveryPlanCard(plan: RecoveryPlanItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = plan.icon,
                    contentDescription = plan.title,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AssistChip(
                        onClick = { },
                        label = { Text(plan.difficulty) }
                    )
                    if (plan.estimatedTime > 0) {
                        AssistChip(
                            onClick = { },
                            label = { Text("${plan.estimatedTime} min") }
                        )
                    }
                }
            }

            FilledTonalButton(
                onClick = { /* Start plan */ }
            ) {
                Text("Start")
            }
        }
    }
}
