package com.burnouttracker.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock Firebase Auth for development without google-services.json
 */
@Singleton
class MockFirebaseAuth @Inject constructor() {
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    /**
     * Check if running in mock mode
     */
    fun isMockMode(): Boolean {
        return try {
            FirebaseAuth.getInstance()
            false
        } catch (e: Exception) {
            true
        }
    }

    /**
     * Mock sign in with email/password
     */
    suspend fun signInWithEmail(email: String, password: String): Result<Boolean> {
        return if (isMockMode()) {
            // Mock authentication - accept any valid format
            if (email.contains("@") && password.length >= 3) {
                _isSignedIn.value = true
                _currentUser.value = MockFirebaseUser(email)
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } else {
            // Real Firebase auth
            try {
                val auth = FirebaseAuth.getInstance()
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _currentUser.value = result.user
                _isSignedIn.value = true
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Mock sign up with email/password
     */
    suspend fun signUpWithEmail(email: String, password: String): Result<Boolean> {
        return if (isMockMode()) {
            if (email.contains("@") && password.length >= 6) {
                _isSignedIn.value = true
                _currentUser.value = MockFirebaseUser(email)
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid email or password (min 6 chars)"))
            }
        } else {
            try {
                val auth = FirebaseAuth.getInstance()
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                _currentUser.value = result.user
                _isSignedIn.value = true
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Mock Google sign in
     */
    suspend fun signInWithGoogle(): Result<Boolean> {
        return if (isMockMode()) {
            _isSignedIn.value = true
            _currentUser.value = MockFirebaseUser("mock@google.com")
            Result.success(true)
        } else {
            // Real Google sign in would be handled by Activity result
            Result.failure(Exception("Use Google Sign-In button"))
        }
    }

    /**
     * Sign out
     */
    fun signOut() {
        _currentUser.value = null
        _isSignedIn.value = false
        if (!isMockMode()) {
            FirebaseAuth.getInstance().signOut()
        }
    }

    /**
     * Get current user
     */
    fun getCurrentUser(): FirebaseUser? {
        return if (isMockMode()) {
            _currentUser.value
        } else {
            FirebaseAuth.getInstance().currentUser
        }
    }
}

/**
 * Mock Firebase User implementation
 */
class MockFirebaseUser(private val email: String) : FirebaseUser() {
    override fun getUid(): String = "mock_${email.hashCode().toUInt()}"
    override fun getEmail(): String = email
    override fun getDisplayName(): String = email.substringBefore("@")
    override fun isEmailVerified(): Boolean = true
    override fun getProviderData(): MutableList<com.google.firebase.auth.UserInfo> = mutableListOf()
    override fun getTenantId(): String? = null
    override fun getPhoneNumber(): String? = null
    override fun getPhotoUrl(): android.net.Uri? = null
    override fun <T : com.google.firebase.auth.UserInfo> getIdToken(forceRefresh: Boolean): com.google.android.gms.tasks.Task<com.google.firebase.auth.GetTokenResult> {
        val result = com.google.firebase.auth.GetTokenResult("mock_token_${System.currentTimeMillis()}")
        return com.google.android.gms.tasks.Tasks.forResult(result)
    }
    override fun linkWithCredential(credential: com.google.firebase.auth.AuthCredential): com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> {
        return com.google.android.gms.tasks.Tasks.forException(Exception("Mock mode"))
    }
    override fun unlink(providerId: String): com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> {
        return com.google.android.gms.tasks.Tasks.forException(Exception("Mock mode"))
    }
    override fun updateEmail(email: String): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun updatePassword(password: String): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun updateProfile(request: com.google.firebase.auth.UserProfileChangeRequest): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun updatePhoneNumber(credential: com.google.firebase.auth.PhoneAuthCredential): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forException(Exception("Mock mode"))
    }
    override fun delete(): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun sendEmailVerification(): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun sendEmailVerification(actionCodeSettings: com.google.firebase.auth.ActionCodeSettings): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun verifyBeforeUpdateEmail(newEmail: String): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
    override fun verifyBeforeUpdateEmail(newEmail: String, actionCodeSettings: com.google.firebase.auth.ActionCodeSettings): com.google.android.gms.tasks.Task<Void> {
        return com.google.android.gms.tasks.Tasks.forResult(null)
    }
}

/**
 * Extension function to await Task result
 */
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return com.google.android.gms.tasks.Tasks.await(this)
}
