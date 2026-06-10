package com.burnouttracker.domain.model

/**
 * Network effect insights based on aggregated anonymized user data
 */
data class NetworkInsights(
    val userProfile: UserInsightProfile,
    val peerComparison: PeerComparison,
    val personalizedRecommendations: List<PersonalizedRecommendation>,
    val trendInsights: List<TrendInsight>
)

/**
 * User's insight profile based on their data
 */
data class UserInsightProfile(
    val stressLevel: String, // "low", "moderate", "high", "critical"
    val spendingPattern: String, // "impulse", "planned", "mixed"
    val topTriggers: List<String>,
    val recoveryStyle: String, // "breathing", "social", "physical", "mindful"
    val averageRecoveryTime: Int // minutes to reduce stress
)

/**
 * Comparison with peer group
 */
data class PeerComparison(
    val totalUsers: Int,
    val usersWithSimilarProfile: Int,
    val spendingComparison: String, // "spend 20% less than similar users"
    val stressComparison: String, // "handle stress better than 60% of users"
    val recoveryComparison: String // "recover faster than 70% of users"
)

/**
 * Personalized recommendation based on network data
 */
data class PersonalizedRecommendation(
    val id: String,
    val title: String,
    val description: String,
    val reason: String, // "Users like you who did X saw Y% improvement"
    val confidence: Float, // 0.0 to 1.0
    val category: RecommendationCategory,
    val expectedImpact: String
)

enum class RecommendationCategory {
    BREATHING,
    MINDFULNESS,
    FINANCIAL,
    SOCIAL,
    PHYSICAL
}

/**
 * Trend insight from network data
 */
data class TrendInsight(
    val title: String,
    val description: String,
    val percentage: Float,
    val trendDirection: TrendDirection,
    val icon: String
)

enum class TrendDirection {
    IMPROVING,
    STABLE,
    WORSENING
}

/**
 * Network effect manager for providing personalized insights
 */
class NetworkInsightsManager {
    
    // Simulated network data - in production, this would come from Firebase/Backend
    private val networkData = NetworkData(
        totalUsers = 15000,
        averageStressSpending = 127.0,
        commonTriggers = listOf("Rent/Mortgage", "Credit Card", "Food Delivery", "Shopping"),
        recoveryActionEffectiveness = mapOf(
            "breathing" to 0.72,
            "mindfulness" to 0.68,
            "physical" to 0.65,
            "social" to 0.58
        ),
        peerGroupData = mapOf(
            "low_stress" to PeerGroupData(avgSpending = 85.0, recoveryRate = 0.85),
            "moderate_stress" to PeerGroupData(avgSpending = 127.0, recoveryRate = 0.65),
            "high_stress" to PeerGroupData(avgSpending = 189.0, recoveryRate = 0.45),
            "critical_stress" to PeerGroupData(avgSpending = 245.0, recoveryRate = 0.30)
        )
    )
    
    /**
     * Generate personalized insights for a user
     */
    fun generateInsights(userStats: CycleBreakerStats, stressLevel: Int): NetworkInsights {
        val userProfile = analyzeUserProfile(userStats, stressLevel)
        val peerComparison = generatePeerComparison(userProfile)
        val recommendations = generateRecommendations(userProfile, userStats)
        val trends = generateTrends(userStats)
        
        return NetworkInsights(
            userProfile = userProfile,
            peerComparison = peerComparison,
            personalizedRecommendations = recommendations,
            trendInsights = trends
        )
    }
    
    private fun analyzeUserProfile(stats: CycleBreakerStats, stressLevel: Int): UserInsightProfile {
        val stressCategory = when {
            stressLevel <= 3 -> "low"
            stressLevel <= 5 -> "moderate"
            stressLevel <= 7 -> "high"
            else -> "critical"
        }
        
        val spendingPattern = when {
            stats.cyclesBroken > 5 -> "planned"
            stats.cyclesBroken > 2 -> "mixed"
            else -> "impulse"
        }
        
        val topTriggers = networkData.commonTriggers.take(3)
        
        val recoveryStyle = when {
            stats.recoveryActions > 10 -> "mindful"
            stats.recoveryActions > 5 -> "breathing"
            stats.recoveryActions > 2 -> "physical"
            else -> "social"
        }
        
        val avgRecoveryTime = when (stressCategory) {
            "low" -> 5
            "moderate" -> 10
            "high" -> 15
            else -> 20
        }
        
        return UserInsightProfile(
            stressLevel = stressCategory,
            spendingPattern = spendingPattern,
            topTriggers = topTriggers,
            recoveryStyle = recoveryStyle,
            averageRecoveryTime = avgRecoveryTime
        )
    }
    
    private fun generatePeerComparison(userProfile: UserInsightProfile): PeerComparison {
        val peerGroup = networkData.peerGroupData[userProfile.stressLevel] ?: networkData.peerGroupData["moderate_stress"]!!
        
        val spendingDiff = ((peerGroup.avgSpending - networkData.averageStressSpending) / networkData.averageStressSpending * 100).toInt()
        val stressDiff = (userProfile.averageRecoveryTime - 12) // Average recovery time
        val recoveryDiff = ((peerGroup.recoveryRate - 0.6) * 100).toInt() // Average recovery rate
        
        return PeerComparison(
            totalUsers = networkData.totalUsers,
            usersWithSimilarProfile = (networkData.totalUsers * 0.15).toInt(),
            spendingComparison = when {
                spendingDiff > 10 -> "Spend ${spendingDiff}% more than similar users"
                spendingDiff < -10 -> "Spend ${-spendingDiff}% less than similar users"
                else -> "Spend about the same as similar users"
            },
            stressComparison = when {
                stressDiff > 3 -> "Handle stress slower than similar users"
                stressDiff < -3 -> "Handle stress faster than similar users"
                else -> "Handle stress about the same as similar users"
            },
            recoveryComparison = when {
                recoveryDiff > 10 -> "Recover ${recoveryDiff}% better than similar users"
                recoveryDiff < -10 -> "Recover ${-recoveryDiff}% worse than similar users"
                else -> "Recover about the same as similar users"
            }
        )
    }
    
    private fun generateRecommendations(
        userProfile: UserInsightProfile,
        stats: CycleBreakerStats
    ): List<PersonalizedRecommendation> {
        val recommendations = mutableListOf<PersonalizedRecommendation>()
        
        // Breathing recommendation
        if (userProfile.stressLevel in listOf("high", "critical")) {
            val effectiveness = networkData.recoveryActionEffectiveness["breathing"] ?: 0.7
            recommendations.add(
                PersonalizedRecommendation(
                    id = "breathing_exercise",
                    title = "Try the 3-Minute Breathing Exercise",
                    description = "A guided breathing technique to reduce stress quickly",
                    reason = "Users like you who did breathing exercises reduced stress spending by ${(effectiveness * 100).toInt()}%",
                    confidence = effectiveness.toFloat(),
                    category = RecommendationCategory.BREATHING,
                    expectedImpact = "Reduce stress spending by 25-35%"
                )
            )
        }
        
        // Mindfulness recommendation
        if (stats.triggersCaught < 5) {
            val effectiveness = networkData.recoveryActionEffectiveness["mindfulness"] ?: 0.68
            recommendations.add(
                PersonalizedRecommendation(
                    id = "mindful_spending",
                    title = "Practice Mindful Spending",
                    description = "Before any purchase, pause and ask: 'Is this need or impulse?'",
                    reason = "Users who practiced mindful spending caught 40% more triggers",
                    confidence = effectiveness.toFloat(),
                    category = RecommendationCategory.MINDFULNESS,
                    expectedImpact = "Catch 2x more spending triggers"
                )
            )
        }
        
        // Social support recommendation
        if (stats.cyclesBroken < 3) {
            val effectiveness = networkData.recoveryActionEffectiveness["social"] ?: 0.58
            recommendations.add(
                PersonalizedRecommendation(
                    id = "social_accountability",
                    title = "Share Your Goal with a Friend",
                    description = "Tell someone about your stress-spending pattern",
                    reason = "Users with accountability partners broke 2x more cycles",
                    confidence = effectiveness.toFloat(),
                    category = RecommendationCategory.SOCIAL,
                    expectedImpact = "Break 2x more stress-spending cycles"
                )
            )
        }
        
        // Physical activity recommendation
        if (userProfile.stressLevel == "critical") {
            val effectiveness = networkData.recoveryActionEffectiveness["physical"] ?: 0.65
            recommendations.add(
                PersonalizedRecommendation(
                    id = "physical_activity",
                    title = "Take a 10-Minute Walk",
                    description = "Physical activity reduces cortisol and stress hormones",
                    reason = "Users who walked before shopping reduced impulse buys by 30%",
                    confidence = effectiveness.toFloat(),
                    category = RecommendationCategory.PHYSICAL,
                    expectedImpact = "Reduce impulse purchases by 30%"
                )
            )
        }
        
        // Financial recommendation
        if (stats.totalSaved < 50) {
            recommendations.add(
                PersonalizedRecommendation(
                    id = "savings_challenge",
                    title = "Try the $50 Savings Challenge",
                    description = "Save $50 this week by avoiding stress spending",
                    reason = "Users who completed this challenge saved an average of $127/month",
                    confidence = 0.75f,
                    category = RecommendationCategory.FINANCIAL,
                    expectedImpact = "Save $50 this week, $127/month average"
                )
            )
        }
        
        return recommendations.take(4) // Return top 4 recommendations
    }
    
    private fun generateTrends(stats: CycleBreakerStats): List<TrendInsight> {
        val trends = mutableListOf<TrendInsight>()
        
        // Stress trend
        if (stats.currentStreak > 0) {
            trends.add(
                TrendInsight(
                    title = "Stress Level Trend",
                    description = "Your stress levels are improving",
                    percentage = (stats.currentStreak * 5f).coerceAtMost(50f),
                    trendDirection = TrendDirection.IMPROVING,
                    icon = "📈"
                )
            )
        }
        
        // Spending trend
        if (stats.totalSaved > 0) {
            trends.add(
                TrendInsight(
                    title = "Spending Pattern",
                    description = "You're spending less on impulse purchases",
                    percentage = (stats.totalSaved.toFloat() / 127f * 100).coerceAtMost(100f),
                    trendDirection = TrendDirection.IMPROVING,
                    icon = "💰"
                )
            )
        }
        
        // Recovery trend
        if (stats.recoveryActions > 0) {
            trends.add(
                TrendInsight(
                    title = "Recovery Effectiveness",
                    description = "Your recovery methods are working",
                    percentage = (stats.recoveryActions * 10f).coerceAtMost(80f),
                    trendDirection = TrendDirection.IMPROVING,
                    icon = "🧘"
                )
            )
        }
        
        return trends
    }
}

/**
 * Internal network data structure
 */
private data class NetworkData(
    val totalUsers: Int,
    val averageStressSpending: Double,
    val commonTriggers: List<String>,
    val recoveryActionEffectiveness: Map<String, Double>,
    val peerGroupData: Map<String, PeerGroupData>
)

private data class PeerGroupData(
    val avgSpending: Double,
    val recoveryRate: Double
)
