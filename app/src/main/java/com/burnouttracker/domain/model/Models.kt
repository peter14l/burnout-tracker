package com.burnouttracker.domain.model

/**
 * User domain model
 */
data class User(
    val id: String,
    val email: String,
    val displayName: String?,
    val createdAt: Long,
    val baselineScore: Int? = null,
    val profileComplete: Boolean = false
)

/**
 * Stress entry for daily check-ins
 */
data class StressEntry(
    val id: String,
    val userId: String,
    val score: Int, // 1-10
    val timestamp: Long,
    val triggers: List<String> = emptyList(),
    val note: String? = null,
    val mood: Mood? = null
)

/**
 * Mood options for journal entries
 */
enum class Mood(val displayName: String, val emoji: String) {
    ANXIOUS("Anxious", "😰"),
    STRESSED("Stressed", "😫"),
    OVERWHELMED("Overwhelmed", "😵"),
    NEUTRAL("Neutral", "😐"),
    CALM("Calm", "😌"),
    HOPEFUL("Hopeful", "🙂"),
    RELIEVED("Relieved", "😊")
}

/**
 * Expense entry
 */
data class Expense(
    val id: String,
    val userId: String,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val timestamp: Long,
    val linkedStressEntryId: String? = null
)

/**
 * Expense categories
 */
enum class ExpenseCategory(val displayName: String) {
    HOUSING("Housing"),
    FOOD("Food & Dining"),
    TRANSPORT("Transport"),
    ENTERTAINMENT("Entertainment"),
    UTILITIES("Utilities"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    PERSONAL("Personal Care"),
    GIFTS("Gifts & Donations"),
    SHOPPING("Shopping"),
    OTHER("Other")
}

/**
 * Recovery plan
 */
data class RecoveryPlan(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val actions: List<RecoveryAction>,
    val difficulty: Difficulty,
    val estimatedTimeMinutes: Int,
    val category: RecoveryCategory
)

/**
 * Individual recovery action
 */
data class RecoveryAction(
    val id: String,
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val completedAt: Long? = null
)

/**
 * Difficulty levels
 */
enum class Difficulty(val displayName: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

/**
 * Recovery categories
 */
enum class RecoveryCategory(val displayName: String) {
    BREATHING("Breathing"),
    MINDFULNESS("Mindfulness"),
    FINANCIAL("Financial"),
    SOCIAL("Social"),
    PHYSICAL("Physical")
}

/**
 * Burnout insight
 */
data class BurnoutInsight(
    val averageStress: Double,
    val trend: Trend,
    val topTriggers: List<TriggerCount>,
    val burnoutRisk: BurnoutRisk,
    val weeklyChange: Double,
    val monthlyChange: Double
)

/**
 * Trend direction
 */
enum class Trend(val displayName: String) {
    IMPROVING("Improving"),
    STABLE("Stable"),
    WORSENING("Worsening")
}

/**
 * Trigger with count
 */
data class TriggerCount(
    val trigger: String,
    val count: Int,
    val percentage: Double
)

/**
 * Burnout risk level
 */
enum class BurnoutRisk(val displayName: String, val description: String) {
    LOW("Low Risk", "You're doing well! Keep up the good habits."),
    MODERATE("Moderate Risk", "Consider using some recovery tools."),
    HIGH("High Risk", "Time to prioritize your financial wellness."),
    CRITICAL("Critical Risk", "Please reach out for support if needed.")
}

/**
 * Onboarding state
 */
data class OnboardingState(
    val step: OnboardingStep,
    val isComplete: Boolean = false
)

/**
 * Onboarding steps
 */
enum class OnboardingStep {
    WELCOME,
    EXPLANATION,
    ASSESSMENT,
    PROFILE
}

/**
 * App state for the whole app
 */
data class AppState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isOnboarded: Boolean = false,
    val error: String? = null
)
