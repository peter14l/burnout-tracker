package com.burnouttracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.StressEntry
import com.burnouttracker.domain.usecase.CalculateBurnoutScoreUseCase
import com.burnouttracker.domain.usecase.GetStressInsightsUseCase
import com.burnouttracker.domain.repository.StressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val latestStress: Int = 0,
    val streakDays: Int = 0,
    val hasCheckedInToday: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stressRepository: StressRepository,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            val userId = mockFirebaseAuth.getUid()

            // Get latest stress entry
            stressRepository.getLatestStressEntry(userId).collect { entry ->
                val todayStart = getTodayStart()
                val hasCheckedInToday = entry?.let { it.timestamp >= todayStart } ?: false

                _uiState.value = _uiState.value.copy(
                    latestStress = entry?.score ?: 0,
                    hasCheckedInToday = hasCheckedInToday,
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            val userId = mockFirebaseAuth.getUid()

            // Calculate streak
            val entries = stressRepository.getStressEntries(userId).first()
            val streak = calculateStreak(entries)
            _uiState.value = _uiState.value.copy(streakDays = streak)
        }
    }

    private fun getTodayStart(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun calculateStreak(entries: List<StressEntry>): Int {
        if (entries.isEmpty()) return 0

        val todayStart = getTodayStart()
        var streak = 0
        var checkDate = todayStart

        // Check if there's an entry for today or yesterday to start counting
        val latestEntry = entries.maxByOrNull { it.timestamp } ?: return 0
        val latestDay = getDayStart(latestEntry.timestamp)

        // If latest entry is not today or yesterday, streak is 0
        if (latestDay < todayStart - 86400000L) return 0

        // Start from today and go backwards
        checkDate = todayStart
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
