package com.burnouttracker.ui.checkin

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.burnouttracker.ui.theme.getStressColor
import com.burnouttracker.ui.theme.getStressLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    onComplete: () -> Unit,
    onBack: () -> Unit,
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onComplete()
        }
    }

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

            Text(
                text = "How's your financial stress today?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    color = getStressColor(uiState.stressScore).copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.stressScore}",
                                style = MaterialTheme.typography.displayLarge,
                                color = getStressColor(uiState.stressScore),
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

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = getStressColor(uiState.stressScore).copy(alpha = 0.1f)
            ) {
                Text(
                    text = getStressLabel(uiState.stressScore),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = getStressColor(uiState.stressScore)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Slider(
                value = uiState.stressScore.toFloat(),
                onValueChange = { viewModel.updateStressScore(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = getStressColor(uiState.stressScore),
                    activeTrackColor = getStressColor(uiState.stressScore)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Low", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("High", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "What triggered this? (optional)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(triggers) { trigger ->
                    FilterChip(
                        selected = trigger in uiState.selectedTriggers,
                        onClick = { viewModel.toggleTrigger(trigger) },
                        label = { Text(trigger) },
                        leadingIcon = if (trigger in uiState.selectedTriggers) {
                            { Icon(Icons.Default.Check, contentDescription = "Selected", modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(moods) { (emoji, label) ->
                    FilterChip(
                        selected = uiState.selectedMood == label,
                        onClick = { viewModel.selectMood(if (uiState.selectedMood == label) null else label) },
                        label = { Text("$emoji $label") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.note,
                onValueChange = { viewModel.updateNote(it) },
                label = { Text("Add a note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.saveCheckIn() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Check-in", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
