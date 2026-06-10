package com.burnouttracker.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.StressEntry
import com.burnouttracker.domain.repository.StressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JournalUiState(
    val entries: List<StressEntry> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val stressRepository: StressRepository,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            val userId = mockFirebaseAuth.getUid()

            stressRepository.getStressEntries(userId).collect { entries ->
                _uiState.value = _uiState.value.copy(
                    entries = entries,
                    isLoading = false
                )
            }
        }
    }
}
