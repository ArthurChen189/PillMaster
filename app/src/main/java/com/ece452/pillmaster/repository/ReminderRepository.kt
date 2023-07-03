package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.Reminder
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

interface IReminderRepository {
    val userid: String
    val reminders: Flow<List<Reminder>>
    suspend fun getReminder(reminderId: String): Reminder?
    suspend fun save(reminder: Reminder): String
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: String)
}

class ReminderRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: IReminderRepository {
    override val userid: String = auth.getUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val reminders: Flow<List<Reminder>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(RECEIVER_COLLECTION).document(user.id).collection(REMINDER_COLLECTION).dataObjects()
        }

    override suspend fun getReminder(reminderId: String): Reminder? =
        firestore.collection(RECEIVER_COLLECTION).document(userid).collection(REMINDER_COLLECTION).document(reminderId).get().await().toObject()

    override suspend fun save(reminder: Reminder): String {
        val pillRef = firestore.collection(RECEIVER_COLLECTION).document(userid).collection(PILL_COLLECTION).whereEqualTo("name", reminder.pillName).limit(1).get().await().reference
        if (pillRef == null || !pillRef.exists()) {
            val newPill = Pill()
            newPill.name = reminder.name
            newPill.description = reminder.description
            firestore.collection(RECEIVER_COLLECTION).document(userid).collection(PILL_COLLECTION).add(newPill).await()
        }
        val reminderWithUserId = reminder.copy(userId = userid)
        return firestore.collection(RECEIVER_COLLECTION).document(userid).collection(REMINDER_COLLECTION).add(reminderWithUserId).await().id
    }

    override suspend fun update(reminder: Reminder) {
        firestore.collection(RECEIVER_COLLECTION).document(userid).collection(REMINDER_COLLECTION).document(reminder.id).set(reminder).await()
    }

    override suspend fun delete(reminderId: String) {
        firestore.collection(RECEIVER_COLLECTION).document(userid).collection(REMINDER_COLLECTION).document(reminderId).delete().await()
    }

    companion object {
        private const val RECEIVER_COLLECTION = "receivers"
        private const val REMINDER_COLLECTION = "reminders"
        private const val PILL_COLLECTION = "pills"
    }
}