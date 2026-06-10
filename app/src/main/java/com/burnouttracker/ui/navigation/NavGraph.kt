package com.burnouttracker.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.ui.auth.LoginScreen
import com.burnouttracker.ui.auth.SignUpScreen
import com.burnouttracker.ui.home.HomeScreen
import com.burnouttracker.ui.checkin.CheckInScreen
import com.burnouttracker.ui.expense.ExpenseScreen
import com.burnouttracker.ui.journal.JournalScreen
import com.burnouttracker.ui.insights.InsightsScreen
import com.burnouttracker.ui.profile.ProfileScreen
import com.burnouttracker.ui.recovery.RecoveryScreen
import com.burnouttracker.ui.onboarding.OnboardingScreen

/**
 * Main navigation graph
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    mockFirebaseAuth: MockFirebaseAuth
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { 30 },
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { -30 },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { -30 },
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { 30 },
                animationSpec = tween(300)
            )
        }
    ) {
        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                mockFirebaseAuth = mockFirebaseAuth
            )
        }

        // Sign Up
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                mockFirebaseAuth = mockFirebaseAuth
            )
        }

        // Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onCheckIn = { navController.navigate(Screen.CheckIn.route) },
                onViewInsights = { navController.navigate(Screen.Insights.route) },
                onViewRecovery = { navController.navigate(Screen.Recovery.route) }
            )
        }

        // Check-in
        composable(Screen.CheckIn.route) {
            CheckInScreen(
                onComplete = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // Expense
        composable(Screen.Expense.route) {
            ExpenseScreen(
                onComplete = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // Journal
        composable(Screen.Journal.route) {
            JournalScreen(
                onEntryClick = { /* Navigate to entry detail */ }
            )
        }

        // Insights
        composable(Screen.Insights.route) {
            InsightsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Recovery
        composable(Screen.Recovery.route) {
            RecoveryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Profile
        composable(Screen.Profile.route) {
            ProfileScreen(
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Settings
        composable(Screen.Settings.route) {
            // SettingsScreen placeholder
        }
    }
}
