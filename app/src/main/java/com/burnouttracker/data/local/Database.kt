package com.burnouttracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Room database for Burnout Tracker
 */
@Database(
    entities = [
        StressEntryEntity::class,
        ExpenseEntity::class,
        RecoveryPlanEntity::class,
        CompletedActionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stressEntryDao(): StressEntryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun recoveryPlanDao(): RecoveryPlanDao
    abstract fun completedActionDao(): CompletedActionDao
}

/**
 * Database provider using Hilt
 */
@Singleton
class DatabaseProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var database: AppDatabase? = null

    fun getDatabase(): AppDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "burnout_tracker_db"
            )
                .fallbackToDestructiveMigration()
                .build()
            database = instance
            instance
        }
    }
}
