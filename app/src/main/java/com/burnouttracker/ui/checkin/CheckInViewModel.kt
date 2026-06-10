package com.burnouttracker.ui.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.Mood
import com.burnouttracker.domain.model.StressEntry
import com.burnouttracker.domain.usecase.RecordStressCheckInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckInUiState(
    val stressScore: Int = 5,
    val selectedTriggers: Set<String> = emptySet(),
    val note: String = "",
    val selectedMood: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val recordStressCheckInUseCase: RecordStressCheckInUseCase,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    fun updateStressScore(score: Int) {
        _uiState.value = _uiState.value.copy(stressScore = score)
    }

    fun toggleTrigger(trigger: String) {
        val current = _uiState.value.selectedTriggers
        _uiState.value = _uiState.value.copy(
            selectedTriggers = if (trigger in current) current - trigger else current + trigger
        )
    }

    fun updateNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    fun selectMood(mood: String?) {
        _uiState.value = _uiState.value.copy(selectedMood = mood)
    }

    fun saveCheckIn() {
        val state = _uiState.value
        if (state.isSaving) return

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null)

            val userId = mockFirebaseAuth.getCurrentUser()?.uid ?: "mock_user"
            val mood = state.selectedMood?.let { moodName ->
                Mood.entries.find { it.displayName == moodName }
            }

            val result = recordStressCheckInUseCase(
                userId = userId,
                score = state.stressScore,
                triggers = state.selectedTriggers.toList(),
                note = state.note.ifBlank { null },
                mood = mood
            )

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isSaving = false, isSaved = true)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to save"
                    )
                }
            )
        }
    }
}
