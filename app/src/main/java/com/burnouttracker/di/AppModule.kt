package com.burnouttracker.di

import android.content.Context
import com.burnouttracker.data.local.*
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.data.remote.MockFirestore
import com.burnouttracker.data.repository.*
import com.burnouttracker.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseProvider(
        @ApplicationContext context: Context
    ): DatabaseProvider {
        return DatabaseProvider(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        databaseProvider: DatabaseProvider
    ): AppDatabase {
        return databaseProvider.getDatabase()
    }

    @Provides
    @Singleton
    fun provideStressEntryDao(
        database: AppDatabase
    ): StressEntryDao {
        return database.stressEntryDao()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(
        database: AppDatabase
    ): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideRecoveryPlanDao(
        database: AppDatabase
    ): RecoveryPlanDao {
        return database.recoveryPlanDao()
    }

    @Provides
    @Singleton
    fun provideCompletedActionDao(
        database: AppDatabase
    ): CompletedActionDao {
        return database.completedActionDao()
    }

    @Provides
    @Singleton
    fun provideStressRepository(
        stressEntryDao: StressEntryDao
    ): StressRepository {
        return StressRepositoryImpl(stressEntryDao)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }

    @Provides
    @Singleton
    fun provideRecoveryRepository(
        recoveryPlanDao: RecoveryPlanDao,
        completedActionDao: CompletedActionDao
    ): RecoveryRepository {
        return RecoveryRepositoryImpl(recoveryPlanDao, completedActionDao)
    }

    @Provides
    @Singleton
    fun provideMockFirebaseAuth(): MockFirebaseAuth {
        return MockFirebaseAuth()
    }

    @Provides
    @Singleton
    fun provideMockFirestore(): MockFirestore {
        return MockFirestore()
    }
}
