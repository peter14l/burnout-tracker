package com.burnouttracker.domain.usecase

import com.burnouttracker.domain.model.*
import com.burnouttracker.domain.repository.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for recording daily stress check-ins
 */
class RecordStressCheckInUseCase @Inject constructor(
    private val stressRepository: StressRepository
) {
    suspend operator fun invoke(
        userId: String,
        score: Int,
        triggers: List<String> = emptyList(),
        note: String? = null,
        mood: Mood? = null
    ): Result<StressEntry> {
        return try {
            val entry = StressEntry(
                id = java.util.UUID.randomUUID().toString(),
                userId = userId,
                score = score.coerceIn(1, 10),
                timestamp = System.currentTimeMillis(),
                triggers = triggers,
                note = note,
                mood = mood
            )
            stressRepository.insertStressEntry(entry)
            Result.success(entry)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use case for getting stress insights
 */
class GetStressInsightsUseCase @Inject constructor(
    private val insightRepository: InsightRepository
) {
    operator fun invoke(userId: String): Flow<BurnoutInsight> {
        return insightRepository.getBurnoutInsight(userId)
    }
}

/**
 * Use case for getting stress trend
 */
class GetStressTrendUseCase @Inject constructor(
    private val insightRepository: InsightRepository
) {
    operator fun invoke(userId: String, days: Int = 7): Flow<List<Pair<String, Double>>> {
        return insightRepository.getStressTrend(userId, days)
    }
}

/**
 * Use case for recording expenses
 */
class RecordExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(
        userId: String,
        amount: Double,
        category: ExpenseCategory,
        description: String? = null,
        tags: List<String> = emptyList(),
        linkedStressEntryId: String? = null
    ): Result<Expense> {
        return try {
            val expense = Expense(
                id = java.util.UUID.randomUUID().toString(),
                userId = userId,
                amount = amount,
                category = category,
                description = description,
                tags = tags,
                timestamp = System.currentTimeMillis(),
                linkedStressEntryId = linkedStressEntryId
            )
            expenseRepository.insertExpense(expense)
            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use case for getting recovery plans
 */
class GetRecoveryPlansUseCase @Inject constructor(
    private val recoveryRepository: RecoveryRepository
) {
    operator fun invoke(): Flow<List<RecoveryPlan>> {
        return recoveryRepository.getRecoveryPlans()
    }

    fun getByCategory(category: RecoveryCategory): Flow<List<RecoveryPlan>> {
        return recoveryRepository.getRecoveryPlansByCategory(category)
    }
}

/**
 * Use case for completing a recovery action
 */
class CompleteRecoveryActionUseCase @Inject constructor(
    private val recoveryRepository: RecoveryRepository
) {
    suspend operator fun invoke(planId: String, actionId: String): Result<Unit> {
        return try {
            recoveryRepository.completeAction(planId, actionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use case for getting user profile
 */
class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<User?> {
        return userRepository.getUser(userId)
    }

    fun getCurrentUser(): Flow<User?> {
        return userRepository.getCurrentUser()
    }
}

/**
 * Use case for updating user profile
 */
class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return try {
            userRepository.updateUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use case for baseline assessment
 */
class CompleteBaselineAssessmentUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, score: Int): Result<Unit> {
        return try {
            userRepository.updateBaselineScore(userId, score)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use case for calculating burnout score
 */
class CalculateBurnoutScoreUseCase @Inject constructor(
    private val stressRepository: StressRepository,
    private val insightRepository: InsightRepository
) {
    fun getAverageStress(userId: String, days: Int = 7): Flow<Double> {
        return stressRepository.getStressAverage(userId, days)
    }

    fun getInsight(userId: String): Flow<BurnoutInsight> {
        return insightRepository.getBurnoutInsight(userId)
    }
}
