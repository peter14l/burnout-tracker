package com.burnouttracker.domain.model

/**
 * Achievement for breaking the stress-spending cycle
 */
data class CycleBreakerAchievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val requirement: CycleBreakRequirement,
    val reward: AchievementReward,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

/**
 * Requirement to unlock an achievement
 */
sealed class CycleBreakRequirement {
    data class CyclesBroken(val count: Int) : CycleBreakRequirement()
    data class ConsecutiveDays(val days: Int) : CycleBreakRequirement()
    data class MoneySaved(val amount: Double) : CycleBreakRequirement()
    data class TriggersCaught(val count: Int) : CycleBreakRequirement()
    data class RecoveryActions(val count: Int) : CycleBreakRequirement()
    data class Combined(val requirements: List<CycleBreakRequirement>) : CycleBreakRequirement()
}

/**
 * Reward for unlocking an achievement
 */
data class AchievementReward(
    val title: String,
    val description: String,
    val streakBonus: Int = 0,
    val unlockFeature: String? = null
)

/**
 * Pre-defined cycle breaker achievements
 */
object CycleBreakerAchievements {
    val achievements = listOf(
        // First cycle break
        CycleBreakerAchievement(
            id = "first_cycle_break",
            title = "Cycle Breaker",
            description = "You broke your first stress-spending cycle!",
            icon = "🎉",
            requirement = CycleBreakRequirement.CyclesBroken(1),
            reward = AchievementReward(
                title = "Cycle Breaker",
                description = "You've taken the first step!"
            )
        ),
        
        // 3 cycles broken
        CycleBreakerAchievement(
            id = "three_cycles",
            title = "Momentum Builder",
            description = "Broke 3 stress-spending cycles",
            icon = "🔥",
            requirement = CycleBreakRequirement.CyclesBroken(3),
            reward = AchievementReward(
                title = "Momentum Builder",
                description = "You're building real habits!",
                streakBonus = 2
            )
        ),
        
        // 7 day streak
        CycleBreakerAchievement(
            id = "week_streak",
            title = "Week Warrior",
            description = "7 days of breaking the cycle",
            icon = "⚔️",
            requirement = CycleBreakRequirement.ConsecutiveDays(7),
            reward = AchievementReward(
                title = "Week Warrior",
                description = "A full week of mindful spending!",
                streakBonus = 5
            )
        ),
        
        // Saved $100
        CycleBreakerAchievement(
            id = "saved_100",
            title = "Century Saver",
            description = "Saved $100 from stress spending",
            icon = "💰",
            requirement = CycleBreakRequirement.MoneySaved(100.0),
            reward = AchievementReward(
                title = "Century Saver",
                description = "Your first $100 saved!",
                unlockFeature = "advanced_insights"
            )
        ),
        
        // Caught 10 triggers
        CycleBreakerAchievement(
            id = "trigger_hunter",
            title = "Trigger Hunter",
            description = "Caught 10 spending triggers",
            icon = "🎯",
            requirement = CycleBreakRequirement.TriggersCaught(10),
            reward = AchievementReward(
                title = "Trigger Hunter",
                description = "You know your triggers now!"
            )
        ),
        
        // 5 recovery actions
        CycleBreakerAchievement(
            id = "recovery_master",
            title = "Recovery Master",
            description = "Completed 5 recovery actions",
            icon = "🧘",
            requirement = CycleBreakRequirement.RecoveryActions(5),
            reward = AchievementReward(
                title = "Recovery Master",
                description = "Master of stress relief!",
                unlockFeature = "personalized_plans"
            )
        ),
        
        // 30 day streak
        CycleBreakerAchievement(
            id = "month_master",
            title = "Month Master",
            description = "30 days of breaking the cycle",
            icon = "👑",
            requirement = CycleBreakRequirement.ConsecutiveDays(30),
            reward = AchievementReward(
                title = "Month Master",
                description = "A full month of control!",
                streakBonus = 10,
                unlockFeature = "premium_insights"
            )
        ),
        
        // Saved $500
        CycleBreakerAchievement(
            id = "saved_500",
            title = "Half Grand Hero",
            description = "Saved $500 from stress spending",
            icon = "🏆",
            requirement = CycleBreakRequirement.MoneySaved(500.0),
            reward = AchievementReward(
                title = "Half Grand Hero",
                description = "Half a thousand saved!",
                unlockFeature = "social_features"
            )
        )
    )
    
    /**
     * Check which achievements are unlocked based on user stats
     */
    fun checkAchievements(stats: CycleBreakerStats): List<CycleBreakerAchievement> {
        return achievements.map { achievement ->
            val isUnlocked = when (val req = achievement.requirement) {
                is CycleBreakRequirement.CyclesBroken -> stats.cyclesBroken >= req.count
                is CycleBreakRequirement.ConsecutiveDays -> stats.consecutiveDays >= req.days
                is CycleBreakRequirement.MoneySaved -> stats.totalSaved >= req.amount
                is CycleBreakRequirement.TriggersCaught -> stats.triggersCaught >= req.count
                is CycleBreakRequirement.RecoveryActions -> stats.recoveryActions >= req.count
                is CycleBreakRequirement.Combined -> req.requirements.all { checkSingleRequirement(it, stats) }
            }
            
            achievement.copy(
                isUnlocked = isUnlocked,
                unlockedAt = if (isUnlocked) System.currentTimeMillis() else null
            )
        }
    }
    
    private fun checkSingleRequirement(requirement: CycleBreakRequirement, stats: CycleBreakerStats): Boolean {
        return when (requirement) {
            is CycleBreakRequirement.CyclesBroken -> stats.cyclesBroken >= requirement.count
            is CycleBreakRequirement.ConsecutiveDays -> stats.consecutiveDays >= requirement.days
            is CycleBreakRequirement.MoneySaved -> stats.totalSaved >= requirement.amount
            is CycleBreakRequirement.TriggersCaught -> stats.triggersCaught >= requirement.count
            is CycleBreakRequirement.RecoveryActions -> stats.recoveryActions >= requirement.count
            is CycleBreakRequirement.Combined -> requirement.requirements.all { checkSingleRequirement(it, stats) }
        }
    }
}

/**
 * User's cycle breaker statistics
 */
data class CycleBreakerStats(
    val cyclesBroken: Int = 0,
    val consecutiveDays: Int = 0,
    val totalSaved: Double = 0.0,
    val triggersCaught: Int = 0,
    val recoveryActions: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)

/**
 * Gamification state for the UI
 */
data class GamificationUiState(
    val stats: CycleBreakerStats = CycleBreakerStats(),
    val achievements: List<CycleBreakerAchievement> = emptyList(),
    val newUnlock: CycleBreakerAchievement? = null,
    val showAchievementPopup: Boolean = false
)
