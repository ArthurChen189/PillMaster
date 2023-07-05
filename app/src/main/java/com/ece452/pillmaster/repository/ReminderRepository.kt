package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.Reminder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface IReminderRepository {
    val reminders: Flow<List<Reminder>>
    suspend fun getReminder(reminderId: String): Reminder?
    suspend fun save(reminder: Reminder): String
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: String)
}

class ReminderRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: IReminderRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val reminders: Flow<List<Reminder>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(REMINDER_COLLECTION).whereEqualTo(USER_ID_FIELD, user.userId).dataObjects()
        }

    override suspend fun getReminder(reminderId: String): Reminder? =
        firestore.collection(REMINDER_COLLECTION).document(reminderId).get().await().toObject()

    override suspend fun save(reminder: Reminder): String {
        val reminderWithUserId = reminder.copy(userId = auth.getUserId())
        return firestore.collection(REMINDER_COLLECTION).add(reminderWithUserId).await().id
    }

    override suspend fun update(reminder: Reminder) {
        firestore.collection(REMINDER_COLLECTION).document(reminder.id).set(reminder).await()
    }

    override suspend fun delete(reminderId: String) {
        firestore.collection(REMINDER_COLLECTION).document(reminderId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val REMINDER_COLLECTION = "reminders"
    }
}