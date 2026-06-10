package com.burnouttracker.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock Firestore for development without google-services.json
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

    /**
     * Save document to collection
     */
    suspend fun saveDocument(
        collection: String,
        documentId: String,
        data: Map<String, Any>
    ): Result<Unit> {
        return if (isMockMode()) {
            mockData.getOrPut(collection) { mutableMapOf() }[documentId] = data
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

    /**
     * Get document from collection
     */
    suspend fun getDocument(
        collection: String,
        documentId: String
    ): Result<Map<String, Any>?> {
        return if (isMockMode()) {
            Result.success(mockData[collection]?.get(documentId))
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

    /**
     * Delete document from collection
     */
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

    /**
     * Get all documents from collection
     */
    suspend fun getCollection(
        collection: String
    ): Result<List<Map<String, Any>>> {
        return if (isMockMode()) {
            Result.success(mockData[collection]?.values?.toList() ?: emptyList())
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
