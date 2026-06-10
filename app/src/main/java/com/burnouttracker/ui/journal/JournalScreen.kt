package com.burnouttracker.ui.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.burnouttracker.ui.theme.getStressColor

data class JournalEntry(
    val id: String,
    val date: String,
    val stressScore: Int,
    val mood: String?,
    val triggers: List<String>,
    val note: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    onEntryClick: (String) -> Unit
) {
    val sampleEntries = listOf(
        JournalEntry("1", "Today, 9:30 AM", 6, "😐 Neutral", listOf("Rent", "Food"), null),
        JournalEntry("2", "Yesterday, 8:15 AM", 4, "😌 Calm", listOf("Entertainment"), "Felt better after talking to friend"),
        JournalEntry("3", "Jun 8, 7:45 AM", 8, "😰 Anxious", listOf("Credit Card", "Unexpected Bill"), "Unexpected car repair"),
        JournalEntry("4", "Jun 7, 9:00 AM", 5, "😫 Stressed", listOf("Rent"), null),
        JournalEntry("5", "Jun 6, 8:30 AM", 3, "🙂 Hopeful", emptyList(), "Got paid today")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Journal") },
                actions = {
                    IconButton(onClick = { /* Add new entry */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Entry"
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
            // Summary card
            item {
                SummaryCard(
                    totalEntries = 5,
                    averageStress = 5.2,
                    commonMood = "Neutral"
                )
            }

            // Entries
            items(sampleEntries) { entry ->
                JournalEntryCard(
                    entry = entry,
                    onClick = { onEntryClick(entry.id) }
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
fun SummaryCard(
    totalEntries: Int,
    averageStress: Double,
    commonMood: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                value = "$totalEntries",
                label = "Entries"
            )
            SummaryItem(
                value = String.format("%.1f", averageStress),
                label = "Avg Stress"
            )
            SummaryItem(
                value = commonMood,
                label = "Top Mood"
            )
        }
    }
}

@Composable
fun SummaryItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun JournalEntryCard(
    entry: JournalEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getStressColor(entry.stressScore).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "${entry.stressScore}/10",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = getStressColor(entry.stressScore)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (entry.mood != null) {
                Text(
                    text = entry.mood,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (entry.triggers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entry.triggers.forEach { trigger ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(trigger) }
                        )
                    }
                }
            }

            if (entry.note != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
