package com.burnouttracker.domain.repository

import com.burnouttracker.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for stress entries
 */
interface StressRepository {
    fun getStressEntries(userId: String): Flow<List<StressEntry>>
    fun getStressEntriesByDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<StressEntry>>
    fun getLatestStressEntry(userId: String): Flow<StressEntry?>
    suspend fun insertStressEntry(entry: StressEntry)
    suspend fun updateStressEntry(entry: StressEntry)
    suspend fun deleteStressEntry(entryId: String)
    fun getStressAverage(userId: String, days: Int): Flow<Double>
}

/**
 * Repository interface for expenses
 */
interface ExpenseRepository {
    fun getExpenses(userId: String): Flow<List<Expense>>
    fun getExpensesByDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<Expense>>
    fun getExpensesByCategory(userId: String, category: ExpenseCategory): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expenseId: String)
    fun getTotalSpending(userId: String, days: Int): Flow<Double>
}

/**
 * Repository interface for recovery plans
 */
interface RecoveryRepository {
    fun getRecoveryPlans(): Flow<List<RecoveryPlan>>
    fun getRecoveryPlanById(planId: String): Flow<RecoveryPlan?>
    fun getRecoveryPlansByCategory(category: RecoveryCategory): Flow<List<RecoveryPlan>>
    fun getUserCompletedPlans(userId: String): Flow<List<String>>
    suspend fun completeAction(planId: String, actionId: String)
    suspend fun resetPlan(planId: String)
}

/**
 * Repository interface for user data
 */
interface UserRepository {
    fun getUser(userId: String): Flow<User?>
    fun getCurrentUser(): Flow<User?>
    suspend fun createUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun updateBaselineScore(userId: String, score: Int)
    suspend fun deleteUser(userId: String)
}

/**
 * Repository interface for insights
 */
interface InsightRepository {
    fun getBurnoutInsight(userId: String): Flow<BurnoutInsight>
    fun getStressTrend(userId: String, days: Int): Flow<List<Pair<String, Double>>>
    fun getTopTriggers(userId: String, days: Int): Flow<List<TriggerCount>>
    fun getSpendingMoodCorrelation(userId: String): Flow<Double>
}
