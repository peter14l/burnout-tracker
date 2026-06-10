package com.burnouttracker.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock Firestore for development without real Firebase
 */
@Singleton
class MockFirestore @Inject constructor() {
    private val mockData = mutableMapOf<String, MutableMap<String, Any>>()

    fun isMockMode(): Boolean {
        return try {
            FirebaseFirestore.getInstance()
            false
        } catch (e: Exception) {
            true
        }
    }

    suspend fun saveDocument(
        collection: String,
        documentId: String,
        data: Map<String, Any>
    ): Result<Unit> {
        return if (isMockMode()) {
            mockData.getOrPut(collection) { mutableMapOf() }[documentId] = data.toMutableMap()
            Result.success(Unit)
        } else {
            try {
                FirebaseFirestore.getInstance()
                    .collection(collection)
                    .document(documentId)
                    .set(data)
                    .await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getDocument(
        collection: String,
        documentId: String
    ): Result<Map<String, Any>?> {
        return if (isMockMode()) {
            val doc = mockData[collection]?.get(documentId)
            @Suppress("UNCHECKED_CAST")
            Result.success(doc as? Map<String, Any>)
        } else {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection(collection)
                    .document(documentId)
                    .get()
                    .await()
                Result.success(doc.data)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteDocument(
        collection: String,
        documentId: String
    ): Result<Unit> {
        return if (isMockMode()) {
            mockData[collection]?.remove(documentId)
            Result.success(Unit)
        } else {
            try {
                FirebaseFirestore.getInstance()
                    .collection(collection)
                    .document(documentId)
                    .delete()
                    .await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getCollection(
        collection: String
    ): Result<List<Map<String, Any>>> {
        return if (isMockMode()) {
            @Suppress("UNCHECKED_CAST")
            val docs = mockData[collection]?.values?.mapNotNull { it as? Map<String, Any> } ?: emptyList()
            Result.success(docs)
        } else {
            try {
                val docs = FirebaseFirestore.getInstance()
                    .collection(collection)
                    .get()
                    .await()
                Result.success(docs.documents.mapNotNull { it.data })
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

/**
 * Extension function to await Task result
 */
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return com.google.android.gms.tasks.Tasks.await(this)
}
