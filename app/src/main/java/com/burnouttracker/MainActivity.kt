package com.burnouttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.ui.navigation.NavGraph
import com.burnouttracker.ui.navigation.Screen
import com.burnouttracker.ui.theme.BurnoutTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mockFirebaseAuth: MockFirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BurnoutTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BurnoutTrackerApp(mockFirebaseAuth)
                }
            }
        }
    }
}

@Composable
fun BurnoutTrackerApp(mockFirebaseAuth: MockFirebaseAuth) {
    val navController = rememberNavController()
    var isOnboarded by remember { mutableStateOf(false) }

    val startDestination = Screen.Login.route

    NavGraph(
        navController = navController,
        startDestination = startDestination,
        mockFirebaseAuth = mockFirebaseAuth
    )
}
