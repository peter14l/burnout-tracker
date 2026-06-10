package com.burnouttracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room entity for stress entries
 */
@Entity(tableName = "stress_entries")
data class StressEntryEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val score: Int,
    val timestamp: Long,
    val triggers: List<String>,
    val note: String?,
    val mood: String?
)

/**
 * Room entity for expenses
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val amount: Double,
    val category: String,
    val description: String?,
    val tags: List<String>,
    val timestamp: Long,
    val linkedStressEntryId: String?
)

/**
 * Room entity for recovery plans
 */
@Entity(tableName = "recovery_plans")
data class RecoveryPlanEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val difficulty: String,
    val estimatedTimeMinutes: Int,
    val category: String
)

/**
 * Room entity for completed recovery actions
 */
@Entity(tableName = "completed_actions")
data class CompletedActionEntity(
    @PrimaryKey
    val id: String,
    val planId: String,
    val actionId: String,
    val completedAt: Long
)

/**
 * Type converters for Room
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
