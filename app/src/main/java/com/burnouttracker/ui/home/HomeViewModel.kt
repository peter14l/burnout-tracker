package com.burnouttracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.Expense
import com.burnouttracker.domain.model.StressEntry
import com.burnouttracker.domain.repository.ExpenseRepository
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
    val isLoading: Boolean = true,
    val totalSpending7Days: Double = 0.0,
    val totalSpending30Days: Double = 0.0,
    val spendingTrend: Double = 0.0,
    val recentExpenses: List<Expense> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stressRepository: StressRepository,
    private val expenseRepository: ExpenseRepository,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        val userId = mockFirebaseAuth.getUid()

        // Load stress entries
        viewModelScope.launch {
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
            val entries = stressRepository.getStressEntries(userId).first()
            val streak = calculateStreak(entries)
            _uiState.value = _uiState.value.copy(streakDays = streak)
        }

        // Load expense data for correlation card
        viewModelScope.launch {
            expenseRepository.getTotalSpending(userId, 7).collect { total ->
                _uiState.value = _uiState.value.copy(totalSpending7Days = total)
            }
        }

        viewModelScope.launch {
            expenseRepository.getTotalSpending(userId, 30).collect { total ->
                _uiState.value = _uiState.value.copy(totalSpending30Days = total)
            }
        }

        viewModelScope.launch {
            expenseRepository.getExpenses(userId).collect { expenses ->
                val last7 = expenses.filter {
                    it.timestamp > System.currentTimeMillis() - 7 * 86400000L
                }
                val prev7 = expenses.filter {
                    val now = System.currentTimeMillis()
                    it.timestamp in (now - 14 * 86400000L) until (now - 7 * 86400000L)
                }
                val currentTotal = last7.sumOf { it.amount }
                val prevTotal = prev7.sumOf { it.amount }
                val trend = if (prevTotal > 0) ((currentTotal - prevTotal) / prevTotal) * 100 else 0.0

                _uiState.value = _uiState.value.copy(
                    recentExpenses = expenses.take(5),
                    spendingTrend = trend
                )
            }
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
