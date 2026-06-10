package com.burnouttracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.StressEntry
import com.burnouttracker.domain.repository.ExpenseRepository
import com.burnouttracker.domain.repository.RecoveryRepository
import com.burnouttracker.domain.repository.StressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val joinDate: String = "",
    val totalCheckIns: Int = 0,
    val currentStreak: Int = 0,
    val averageStress: Double = 0.0,
    val plansCompleted: Int = 0,
    val totalSpending: Double = 0.0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val stressRepository: StressRepository,
    private val expenseRepository: ExpenseRepository,
    private val recoveryRepository: RecoveryRepository,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        val userId = mockFirebaseAuth.getUid()

        viewModelScope.launch {
            stressRepository.getStressEntries(userId).collect { entries ->
                val avgStress = if (entries.isNotEmpty()) entries.map { it.score }.average() else 0.0
                val streak = calculateStreak(entries)

                _uiState.value = _uiState.value.copy(
                    totalCheckIns = entries.size,
                    averageStress = avgStress,
                    currentStreak = streak,
                    userName = "Stella", // Mock user name
                    joinDate = "June 2026",
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            recoveryRepository.getUserCompletedPlans(userId).collect { completedIds ->
                _uiState.value = _uiState.value.copy(
                    plansCompleted = completedIds.size
                )
            }
        }

        viewModelScope.launch {
            expenseRepository.getTotalSpending(userId, 30).collect { total ->
                _uiState.value = _uiState.value.copy(
                    totalSpending = total
                )
            }
        }
    }

    private fun calculateStreak(entries: List<StressEntry>): Int {
        if (entries.isEmpty()) return 0

        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis

        val latestEntry = entries.maxByOrNull { it.timestamp } ?: return 0
        val latestDay = getDayStart(latestEntry.timestamp)

        if (latestDay < todayStart - 86400000L) return 0

        var streak = 0
        var checkDate = todayStart

        while (true) {
            val hasEntry = entries.any { entry ->
                getDayStart(entry.timestamp) == checkDate
            }
            if (hasEntry) {
                streak++
                checkDate -= 86400000L
            } else {
                break
            }
        }

        return streak
    }

    private fun getDayStart(timestamp: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = timestamp
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
