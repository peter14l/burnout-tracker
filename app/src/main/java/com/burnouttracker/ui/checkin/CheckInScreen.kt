package com.burnouttracker.ui.checkin

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burnouttracker.ui.theme.BurnoutColors
import com.burnouttracker.ui.theme.getStressColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    var stressScore by remember { mutableIntStateOf(5) }
    var selectedTriggers by remember { mutableStateOf(setOf<String>()) }
    var note by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<String?>(null) }

    val triggers = listOf(
        "Rent/Mortgage",
        "Credit Card",
        "Unexpected Bill",
        "Food Spending",
        "Entertainment",
        "Transport",
        "Shopping",
        "Other"
    )

    val moods = listOf(
        "😰" to "Anxious",
        "😫" to "Stressed",
        "😵" to "Overwhelmed",
        "😐" to "Neutral",
        "😌" to "Calm",
        "🙂" to "Hopeful",
        "😊" to "Relieved"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Check-in") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Question
            Text(
                text = "How's your financial stress today?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Score display
            Box(
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    color = getStressColor(stressScore).copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$stressScore",
                                style = MaterialTheme.typography.displayLarge,
                                color = getStressColor(stressScore),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "/ 10",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stress level label
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = getStressColor(stressScore).copy(alpha = 0.1f)
            ) {
                Text(
                    text = getStressLabel(stressScore),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = getStressColor(stressScore)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Slider
            Slider(
                value = stressScore.toFloat(),
                onValueChange = { stressScore = it.toInt() },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = getStressColor(stressScore),
                    activeTrackColor = getStressColor(stressScore)
                )
            )

            // Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Low",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "High",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Triggers section
            Text(
                text = "What triggered this? (optional)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(triggers) { trigger ->
                    FilterChip(
                        selected = trigger in selectedTriggers,
                        onClick = {
                            selectedTriggers = if (trigger in selectedTriggers) {
                                selectedTriggers - trigger
                            } else {
                                selectedTriggers + trigger
                            }
                        },
                        label = { Text(trigger) },
                        leadingIcon = if (trigger in selectedTriggers) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mood section
            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(moods) { (emoji, label) ->
                    FilterChip(
                        selected = selectedMood == label,
                        onClick = {
                            selectedMood = if (selectedMood == label) null else label
                        },
                        label = { Text("$emoji $label") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Note field
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Add a note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Save Check-in",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
