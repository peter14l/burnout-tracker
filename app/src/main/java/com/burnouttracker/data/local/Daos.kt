package com.burnouttracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for stress entries
 */
@Dao
interface StressEntryDao {
    @Query("SELECT * FROM stress_entries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getStressEntries(userId: String): Flow<List<StressEntryEntity>>

    @Query("SELECT * FROM stress_entries WHERE userId = :userId AND timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getStressEntriesByDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<StressEntryEntity>>

    @Query("SELECT * FROM stress_entries WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestStressEntry(userId: String): Flow<StressEntryEntity?>

    @Query("SELECT AVG(score) FROM stress_entries WHERE userId = :userId AND timestamp > :cutoffTime")
    fun getStressAverage(userId: String, cutoffTime: Long): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStressEntry(entry: StressEntryEntity)

    @Update
    suspend fun updateStressEntry(entry: StressEntryEntity)

    @Query("DELETE FROM stress_entries WHERE id = :entryId")
    suspend fun deleteStressEntry(entryId: String)
}

/**
 * DAO for expenses
 */
@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY timestamp DESC")
    fun getExpenses(userId: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getExpensesByDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND category = :category ORDER BY timestamp DESC")
    fun getExpensesByCategory(userId: String, category: String): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId AND timestamp > :cutoffTime")
    fun getTotalSpending(userId: String, cutoffTime: Long): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: String)
}

/**
 * DAO for recovery plans
 */
@Dao
interface RecoveryPlanDao {
    @Query("SELECT * FROM recovery_plans")
    fun getRecoveryPlans(): Flow<List<RecoveryPlanEntity>>

    @Query("SELECT * FROM recovery_plans WHERE id = :planId")
    fun getRecoveryPlanById(planId: String): Flow<RecoveryPlanEntity?>

    @Query("SELECT * FROM recovery_plans WHERE category = :category")
    fun getRecoveryPlansByCategory(category: String): Flow<List<RecoveryPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecoveryPlan(plan: RecoveryPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecoveryPlans(plans: List<RecoveryPlanEntity>)
}

/**
 * DAO for completed actions
 */
@Dao
interface CompletedActionDao {
    @Query("SELECT DISTINCT planId FROM completed_actions WHERE planId IN (:planIds)")
    fun getCompletedPlanIds(planIds: List<String>): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedAction(action: CompletedActionEntity)

    @Query("DELETE FROM completed_actions WHERE planId = :planId")
    suspend fun deleteCompletedActions(planId: String)
}
