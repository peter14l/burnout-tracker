package com.burnouttracker.ui.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.Difficulty
import com.burnouttracker.domain.model.RecoveryCategory
import com.burnouttracker.domain.model.RecoveryPlan
import com.burnouttracker.domain.repository.RecoveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecoveryUiState(
    val isLoading: Boolean = true,
    val plans: List<RecoveryPlan> = emptyList(),
    val selectedCategory: String = "All",
    val completedPlanIds: Set<String> = emptySet()
)

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val recoveryRepository: RecoveryRepository,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = _uiState.asStateFlow()

    init {
        loadRecoveryPlans()
    }

    private fun loadRecoveryPlans() {
        viewModelScope.launch {
            recoveryRepository.getRecoveryPlans().collect { plans ->
                _uiState.value = _uiState.value.copy(
                    plans = plans,
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            val userId = mockFirebaseAuth.getUid()
            recoveryRepository.getUserCompletedPlans(userId).collect { completedIds ->
                _uiState.value = _uiState.value.copy(
                    completedPlanIds = completedIds.toSet()
                )
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun startPlan(planId: String) {
        viewModelScope.launch {
            // Mark first action as started (simplified)
        }
    }

    fun getFilteredPlans(): List<RecoveryPlan> {
        val state = _uiState.value
        return if (state.selectedCategory == "All") {
            state.plans
        } else {
            state.plans.filter { it.category.displayName == state.selectedCategory }
        }
    }

    companion object {
        val categories = listOf("All", "Breathing", "Financial", "Mindfulness", "Social")
    }
}
