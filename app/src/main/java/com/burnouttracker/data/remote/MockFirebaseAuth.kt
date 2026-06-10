package com.burnouttracker.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock Firebase Auth for development without real Firebase.
 * All auth is local until a real Firebase project is configured.
 */
@Singleton
class MockFirebaseAuth @Inject constructor() {
    private val _currentUserEmail = MutableStateFlow<String?>(null)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail.asStateFlow()

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    fun signInWithEmail(email: String, password: String): Result<Boolean> {
        return if (email.contains("@") && password.length >= 3) {
            _isSignedIn.value = true
            _currentUserEmail.value = email
            Result.success(true)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    fun signUpWithEmail(email: String, password: String): Result<Boolean> {
        return if (email.contains("@") && password.length >= 6) {
            _isSignedIn.value = true
            _currentUserEmail.value = email
            Result.success(true)
        } else {
            Result.failure(Exception("Invalid email or password (min 6 chars)"))
        }
    }

    fun signInWithGoogle(): Result<Boolean> {
        _isSignedIn.value = true
        _currentUserEmail.value = "mock@google.com"
        return Result.success(true)
    }

    fun signOut() {
        _currentUserEmail.value = null
        _isSignedIn.value = false
    }

    fun getCurrentUserEmail(): String? {
        return _currentUserEmail.value
    }

    fun getUid(): String {
        val email = _currentUserEmail.value ?: "anonymous"
        return "mock_${email.hashCode().toUInt()}"
    }
}
