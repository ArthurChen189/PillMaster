package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.Pill
import com.ece452.pillmaster.di.FirebaseModule
import com.ece452.pillmaster.repository.AuthRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface IPillRepository {
    val userid: String
    val pills: Flow<List<Pill>>
    suspend fun getPill(pillId: String): Pill?
    // TODO: On PillManagementScreen, show each pill's name and a button to delete
    suspend fun delete(pillId: String)
}

class PillRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: IPillRepository {
    override val userid: String = auth.getUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val pills: Flow<List<Pill>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(RECEIVER_COLLECTION).document(user.id).collection(PILL_COLLECTION).dataObjects()
        }

    override suspend fun getPill(pillId: String): Pill? =
        firestore.collection(RECEIVER_COLLECTION).document(userid).collection(PILL_COLLECTION).document(pillId).get().await().toObject()

    override suspend fun delete(pillId: String) {
        firestore.collection(RECEIVER_COLLECTION).document(userid).collection(PILL_COLLECTION).document(pillId).delete().await()
    }

    companion object {
        private const val RECEIVER_COLLECTION = "receivers"
        private const val PILL_COLLECTION = "pills"
    }
}