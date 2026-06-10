package com.burnouttracker.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.*
import com.burnouttracker.domain.repository.ExpenseRepository
import com.burnouttracker.domain.repository.StressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsightsUiState(
    val isLoading: Boolean = true,
    val weeklyTrend: List<Pair<String, Double>> = emptyList(),
    val topTriggers: List<TriggerCount> = emptyList(),
    val burnoutRisk: BurnoutRisk = BurnoutRisk.LOW,
    val riskMessage: String = "",
    val averageStress: Double = 0.0,
    val weeklyChange: Double = 0.0,
    val monthlyChange: Double = 0.0,
    val spendingMoodCorrelation: Double = 0.0,
    val recentExpenses: List<Expense> = emptyList(),
    val stressEntries: List<StressEntry> = emptyList()
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val stressRepository: StressRepository,
    private val expenseRepository: ExpenseRepository,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        loadInsightsData()
    }

    private fun loadInsightsData() {
        val userId = mockFirebaseAuth.getUid()

        // Load stress entries
        viewModelScope.launch {
            stressRepository.getStressEntries(userId).collect { entries ->
                val weeklyTrend = calculateWeeklyTrend(entries)
                val topTriggers = calculateTopTriggers(entries)
                val burnoutRisk = calculateBurnoutRisk(entries)
                val riskMessage = getRiskMessage(burnoutRisk)
                val weeklyChange = calculateWeeklyChange(entries)
                val monthlyChange = calculateMonthlyChange(entries)
                val avgStress = if (entries.isNotEmpty()) entries.map { it.score }.average() else 0.0

                _uiState.value = _uiState.value.copy(
                    stressEntries = entries,
                    weeklyTrend = weeklyTrend,
                    topTriggers = topTriggers,
                    burnoutRisk = burnoutRisk,
                    riskMessage = riskMessage,
                    averageStress = avgStress,
                    weeklyChange = weeklyChange,
                    monthlyChange = monthlyChange,
                    isLoading = false
                )
            }
        }

        // Load expenses for correlation
        viewModelScope.launch {
            expenseRepository.getExpenses(userId).collect { expenses ->
                val correlation = calculateSpendingMoodCorrelation(
                    expenses,
                    _uiState.value.stressEntries
                )
                _uiState.value = _uiState.value.copy(
                    recentExpenses = expenses,
                    spendingMoodCorrelation = correlation
                )
            }
        }
    }

    private fun calculateWeeklyTrend(entries: List<StressEntry>): List<Pair<String, Double>> {
        val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val calendar = java.util.Calendar.getInstance()

        return dayNames.map { dayName ->
            val dayIndex = dayNames.indexOf(dayName)
            val targetDay = calendar.clone() as java.util.Calendar
            targetDay.add(java.util.Calendar.DAY_OF_YEAR, -(6 - dayIndex))
            targetDay.set(java.util.Calendar.HOUR_OF_DAY, 0)
            targetDay.set(java.util.Calendar.MINUTE, 0)
            targetDay.set(java.util.Calendar.SECOND, 0)
            targetDay.set(java.util.Calendar.MILLISECOND, 0)
            val dayStart = targetDay.timeInMillis
            val dayEnd = dayStart + 86400000L

            val dayEntries = entries.filter { it.timestamp in dayStart until dayEnd }
            val avgScore = if (dayEntries.isNotEmpty()) {
                dayEntries.map { it.score }.average()
            } else {
                0.0
            }
            dayName to avgScore
        }
    }

    private fun calculateTopTriggers(entries: List<StressEntry>): List<TriggerCount> {
        val triggerMap = mutableMapOf<String, Int>()
        entries.forEach { entry ->
            entry.triggers.forEach { trigger ->
                triggerMap[trigger] = (triggerMap[trigger] ?: 0) + 1
            }
        }
        val total = triggerMap.values.sum().coerceAtLeast(1)
        return triggerMap.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { TriggerCount(it.key, it.value, it.value * 100.0 / total) }
    }

    private fun calculateBurnoutRisk(entries: List<StressEntry>): BurnoutRisk {
        if (entries.isEmpty()) return BurnoutRisk.LOW
        val recentEntries = entries.take(7)
        val avgScore = recentEntries.map { it.score }.average()
        return when {
            avgScore <= 3 -> BurnoutRisk.LOW
            avgScore <= 5 -> BurnoutRisk.MODERATE
            avgScore <= 7 -> BurnoutRisk.HIGH
            else -> BurnoutRisk.CRITICAL
        }
    }

    private fun getRiskMessage(risk: BurnoutRisk): String {
        return when (risk) {
            BurnoutRisk.LOW -> "You're doing great! Keep up the healthy habits."
            BurnoutRisk.MODERATE -> "Consider using some recovery tools this week."
            BurnoutRisk.HIGH -> "Time to prioritize your financial wellness."
            BurnoutRisk.CRITICAL -> "Please reach out for support if needed."
        }
    }

    private fun calculateWeeklyChange(entries: List<StressEntry>): Double {
        val now = System.currentTimeMillis()
        val weekAgo = now - 7 * 86400000L
        val twoWeeksAgo = now - 14 * 86400000L

        val thisWeek = entries.filter { it.timestamp in weekAgo..now }.map { it.score }
        val lastWeek = entries.filter { it.timestamp in twoWeeksAgo until weekAgo }.map { it.score }

        if (thisWeek.isEmpty() || lastWeek.isEmpty()) return 0.0
        return thisWeek.average() - lastWeek.average()
    }

    private fun calculateMonthlyChange(entries: List<StressEntry>): Double {
        val now = System.currentTimeMillis()
        val monthAgo = now - 30 * 86400000L
        val twoMonthsAgo = now - 60 * 86400000L

        val thisMonth = entries.filter { it.timestamp in monthAgo..now }.map { it.score }
        val lastMonth = entries.filter { it.timestamp in twoMonthsAgo until monthAgo }.map { it.score }

        if (thisMonth.isEmpty() || lastMonth.isEmpty()) return 0.0
        return thisMonth.average() - lastMonth.average()
    }

    private fun calculateSpendingMoodCorrelation(
        expenses: List<Expense>,
        stressEntries: List<StressEntry>
    ): Double {
        if (expenses.isEmpty() || stressEntries.isEmpty()) return 0.0

        // Simple correlation: group expenses and stress by day
        val calendar = java.util.Calendar.getInstance()
        val dailyData = mutableMapOf<Long, Pair<Double, Double>>() // day -> (spending, stress)

        expenses.forEach { expense ->
            calendar.timeInMillis = expense.timestamp
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            val day = calendar.timeInMillis
            val current = dailyData[day] ?: (0.0 to 0.0)
            dailyData[day] = (current.first + expense.amount) to current.second
        }

        stressEntries.forEach { entry ->
            calendar.timeInMillis = entry.timestamp
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            val day = calendar.timeInMillis
            val current = dailyData[day] ?: (0.0 to 0.0)
            dailyData[day] = current.first to entry.score.toDouble()
        }

        val pairs = dailyData.values.filter { it.first > 0.0 && it.second > 0.0 }
        if (pairs.size < 2) return 0.0

        // Pearson correlation
        val n = pairs.size
        val sumX = pairs.sumOf { it.first }
        val sumY = pairs.sumOf { it.second }
        val sumXY = pairs.sumOf { it.first * it.second }
        val sumX2 = pairs.sumOf { it.first * it.first }
        val sumY2 = pairs.sumOf { it.second * it.second }

        val numerator = n * sumXY - sumX * sumY
        val denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY))

        return if (denominator == 0.0) 0.0 else (numerator / denominator).coerceIn(-1.0, 1.0)
    }
}
