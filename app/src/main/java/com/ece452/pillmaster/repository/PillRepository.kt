package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.Pill
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface IPillRepository {
    val pills: Flow<List<Pill>>
    
    suspend fun getPill(pillId: String): Pill?

    suspend fun delete(pillId: String)
}

class PillRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: IPillRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val pills: Flow<List<Pill>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(PILL_COLLECTION).whereEqualTo(USER_ID_FIELD, user.userId).dataObjects()
        }

    override suspend fun getPill(pillId: String): Pill? =
        firestore.collection(PILL_COLLECTION).document(pillId).get().await().toObject()

    // When delete a pill, delete all reminders of that pill
    override suspend fun delete(pillId: String) {
        val pillName = getPill(pillId)?.name
        val userid = auth.getUserId()
        val batch = firestore.batch() // Use batch write to improve performance
        val remindersToDelete = firestore.collection(REMINDER_COLLECTION).whereEqualTo(USER_ID_FIELD, userid).whereEqualTo(NAME_FIELD, pillName).get().await().documents
        remindersToDelete.forEach { batch.delete(it.reference) }
        batch.commit().await()
        firestore.collection(PILL_COLLECTION).document(pillId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val NAME_FIELD = "name"
        private const val REMINDER_COLLECTION = "reminders"
        private const val PILL_COLLECTION = "pills"
    }
}