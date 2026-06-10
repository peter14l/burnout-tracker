package com.burnouttracker.data.repository

import com.burnouttracker.data.local.*
import com.burnouttracker.domain.model.*
import com.burnouttracker.domain.repository.StressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of StressRepository using local Room database
 */
@Singleton
class StressRepositoryImpl @Inject constructor(
    private val stressEntryDao: StressEntryDao
) : StressRepository {

    override fun getStressEntries(userId: String): Flow<List<StressEntry>> {
        return stressEntryDao.getStressEntries(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStressEntriesByDateRange(
        userId: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<StressEntry>> {
        return stressEntryDao.getStressEntriesByDateRange(userId, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLatestStressEntry(userId: String): Flow<StressEntry?> {
        return stressEntryDao.getLatestStressEntry(userId).map { it?.toDomain() }
    }

    override suspend fun insertStressEntry(entry: StressEntry) {
        stressEntryDao.insertStressEntry(entry.toEntity())
    }

    override suspend fun updateStressEntry(entry: StressEntry) {
        stressEntryDao.updateStressEntry(entry.toEntity())
    }

    override suspend fun deleteStressEntry(entryId: String) {
        stressEntryDao.deleteStressEntry(entryId)
    }

    override fun getStressAverage(userId: String, days: Int): Flow<Double> {
        val cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        return stressEntryDao.getStressAverage(userId, cutoffTime).map { it ?: 0.0 }
    }

    private fun StressEntryEntity.toDomain() = StressEntry(
        id = id,
        userId = userId,
        score = score,
        timestamp = timestamp,
        triggers = triggers,
        note = note,
        mood = mood?.let { Mood.valueOf(it) }
    )

    private fun StressEntry.toEntity() = StressEntryEntity(
        id = id,
        userId = userId,
        score = score,
        timestamp = timestamp,
        triggers = triggers,
        note = note,
        mood = mood?.name
    )
}

/**
 * Implementation of ExpenseRepository using local Room database
 */
@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : com.burnouttracker.domain.repository.ExpenseRepository {

    override fun getExpenses(userId: String): Flow<List<Expense>> {
        return expenseDao.getExpenses(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getExpensesByDateRange(
        userId: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(userId, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getExpensesByCategory(
        userId: String,
        category: ExpenseCategory
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(userId, category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expenseId: String) {
        expenseDao.deleteExpense(expenseId)
    }

    override fun getTotalSpending(userId: String, days: Int): Flow<Double> {
        val cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        return expenseDao.getTotalSpending(userId, cutoffTime).map { it ?: 0.0 }
    }

    private fun ExpenseEntity.toDomain() = Expense(
        id = id,
        userId = userId,
        amount = amount,
        category = ExpenseCategory.valueOf(category),
        description = description,
        tags = tags,
        timestamp = timestamp,
        linkedStressEntryId = linkedStressEntryId
    )

    private fun Expense.toEntity() = ExpenseEntity(
        id = id,
        userId = userId,
        amount = amount,
        category = category.name,
        description = description,
        tags = tags,
        timestamp = timestamp,
        linkedStressEntryId = linkedStressEntryId
    )
}

/**
 * Implementation of RecoveryRepository using local Room database
 */
@Singleton
class RecoveryRepositoryImpl @Inject constructor(
    private val recoveryPlanDao: RecoveryPlanDao,
    private val completedActionDao: CompletedActionDao
) : com.burnouttracker.domain.repository.RecoveryRepository {

    override fun getRecoveryPlans(): Flow<List<RecoveryPlan>> {
        return recoveryPlanDao.getRecoveryPlans().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRecoveryPlanById(planId: String): Flow<RecoveryPlan?> {
        return recoveryPlanDao.getRecoveryPlanById(planId).map { it?.toDomain() }
    }

    override fun getRecoveryPlansByCategory(category: RecoveryCategory): Flow<List<RecoveryPlan>> {
        return recoveryPlanDao.getRecoveryPlansByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUserCompletedPlans(userId: String): Flow<List<String>> {
        // This would need user-specific tracking in a real implementation
        return kotlinx.coroutines.flow.flowOf(emptyList())
    }

    override suspend fun completeAction(planId: String, actionId: String) {
        completedActionDao.insertCompletedAction(
            CompletedActionEntity(
                id = "${planId}_${actionId}",
                planId = planId,
                actionId = actionId,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun resetPlan(planId: String) {
        completedActionDao.deleteCompletedActions(planId)
    }

    private fun RecoveryPlanEntity.toDomain() = RecoveryPlan(
        id = id,
        title = title,
        description = description,
        icon = icon,
        actions = emptyList(), // Would be loaded separately
        difficulty = Difficulty.valueOf(difficulty),
        estimatedTimeMinutes = estimatedTimeMinutes,
        category = RecoveryCategory.valueOf(category)
    )
}
