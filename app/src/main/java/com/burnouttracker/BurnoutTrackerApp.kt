package com.burnouttracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for Burnout Tracker
 */
@HiltAndroidApp
class BurnoutTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
