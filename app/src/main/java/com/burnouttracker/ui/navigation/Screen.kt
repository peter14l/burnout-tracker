package com.burnouttracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation routes
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object CheckIn : Screen("checkin")
    data object Expense : Screen("expense")
    data object Journal : Screen("journal")
    data object Insights : Screen("insights")
    data object Recovery : Screen("recovery")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
}

/**
 * Bottom navigation items
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomNavItem(
        route = Screen.Home.route,
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Journal : BottomNavItem(
        route = Screen.Journal.route,
        title = "Journal",
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook
    )

    data object Insights : BottomNavItem(
        route = Screen.Insights.route,
        title = "Insights",
        selectedIcon = Icons.Filled.Insights,
        unselectedIcon = Icons.Outlined.Insights
    )

    data object Profile : BottomNavItem(
        route = Screen.Profile.route,
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Journal,
    BottomNavItem.Insights,
    BottomNavItem.Profile
)
