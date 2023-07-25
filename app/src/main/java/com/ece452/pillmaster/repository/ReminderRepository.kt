package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.model.Pill
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.ece452.pillmaster.utils.DrugInfo

// Interface of the Reminder repository
interface IReminderRepository {
    val reminders: Flow<List<Reminder>>
    suspend fun getReminder(reminderId: String): Reminder?
    suspend fun save(reminder: Reminder): String
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: String)
}

// Implementation of the Reminder repository
class ReminderRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: IReminderRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val reminders: Flow<List<Reminder>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(REMINDER_COLLECTION).whereEqualTo(USER_ID_FIELD, user.userId).dataObjects()
        }

    // Get a single reminder from firestore by document id
    override suspend fun getReminder(reminderId: String): Reminder? =
        firestore.collection(REMINDER_COLLECTION).document(reminderId).get().await().toObject()

    // Add a reminder to firestore, if the pill of the reminder name does not exist, add the pill to firestore as well
    override suspend fun save(reminder: Reminder): String {
        val userid = auth.getUserId()
        val pillRef = firestore.collection(PILL_COLLECTION).whereEqualTo(USER_ID_FIELD, userid).whereEqualTo(NAME_FIELD, reminder.name).limit(1).get().await().documents.firstOrNull()
        if (pillRef == null) {
            var newPill = Pill()
            newPill.userId = userid
            newPill.name = reminder.name
            newPill.description = reminder.description
            newPill.info = DrugInfo.get_incompatible_drug_list(reminder.name, 3)
            firestore.collection(PILL_COLLECTION).add(newPill).await()
        }
        val reminderWithUserId = reminder.copy(userId = userid)
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
        private const val NAME_FIELD = "name"
        private const val REMINDER_COLLECTION = "reminders"
        private const val PILL_COLLECTION = "pills"
    }
}