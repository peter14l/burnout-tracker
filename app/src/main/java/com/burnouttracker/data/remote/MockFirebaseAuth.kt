package com.burnouttracker.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock Firebase Auth for development without real Firebase
 */
@Singleton
class MockFirebaseAuth @Inject constructor() {
    private val _currentUserEmail = MutableStateFlow<String?>(null)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail.asStateFlow()

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    fun isMockMode(): Boolean {
        return try {
            FirebaseAuth.getInstance()
            false
        } catch (e: Exception) {
            true
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<Boolean> {
        return if (isMockMode()) {
            if (email.contains("@") && password.length >= 3) {
                _isSignedIn.value = true
                _currentUserEmail.value = email
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } else {
            try {
                val auth = FirebaseAuth.getInstance()
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _currentUserEmail.value = result.user?.email
                _isSignedIn.value = true
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<Boolean> {
        return if (isMockMode()) {
            if (email.contains("@") && password.length >= 6) {
                _isSignedIn.value = true
                _currentUserEmail.value = email
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid email or password (min 6 chars)"))
            }
        } else {
            try {
                val auth = FirebaseAuth.getInstance()
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                _currentUserEmail.value = result.user?.email
                _isSignedIn.value = true
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun signInWithGoogle(): Result<Boolean> {
        return if (isMockMode()) {
            _isSignedIn.value = true
            _currentUserEmail.value = "mock@google.com"
            Result.success(true)
        } else {
            Result.failure(Exception("Use Google Sign-In button"))
        }
    }

    fun signOut() {
        _currentUserEmail.value = null
        _isSignedIn.value = false
        if (!isMockMode()) {
            FirebaseAuth.getInstance().signOut()
        }
    }

    fun getCurrentUserEmail(): String? {
        return _currentUserEmail.value
    }

    fun getUid(): String {
        val email = _currentUserEmail.value ?: "anonymous"
        return "mock_${email.hashCode().toUInt()}"
    }
}


