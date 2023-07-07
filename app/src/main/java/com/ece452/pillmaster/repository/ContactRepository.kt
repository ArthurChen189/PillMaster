package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.CareGiverContactGroup
import com.ece452.pillmaster.model.CareReceiverContactGroup
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.model.ContactGroup
import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.utils.UserRole
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface IContactRepository {
    val sentContactRequests: Flow<List<Contact>>
    val pendingContactRequests: Flow<List<Contact>>
    val connectedContacts: Flow<List<Contact>>
    suspend fun addContact(currentUserId: String, targetUserEmail: String)
    suspend fun removeContact(contact: Contact)
    suspend fun acceptContactRequest(contact: Contact)
}

class CareReceiverContactRepository
@Inject constructor (
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository)
    : IContactRepository
{
    private var careGiverContactGroup: CareGiverContactGroup = CareGiverContactGroup()

    override val sentContactRequests: Flow<List<Contact>>
        get() = careGiverContactGroup.getSentContactRequests(getContacts())

    override val pendingContactRequests: Flow<List<Contact>>
        get() = careGiverContactGroup.getPendingContactRequests(getContacts())

    override val connectedContacts: Flow<List<Contact>>
        get() = careGiverContactGroup.getConnectedContacts(getContacts())

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getContacts(): Flow<List<Contact>> =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(CONTACT_COLLECTION)
                .whereEqualTo(CARE_RECEIVER_ID_FIELD, user.userId)
                .dataObjects()
        }

    override suspend fun addContact(currentUserId: String, targetUserEmail: String) {
        val currentUser = auth.getUserProfileById(currentUserId)
        val targetUser = auth.getUserProfileByEmail(targetUserEmail)
        if (currentUser.userId == targetUser.userId) {
            throw Exception("Cannot add yourself as a contact.")
        }
        val contact = careGiverContactGroup.createContactRequest(currentUser = currentUser, targetUser = targetUser)
        contact.id = firestore.collection(CONTACT_COLLECTION).add(contact).await().id
    }

    override suspend fun removeContact(contact: Contact) {
        firestore.collection(CONTACT_COLLECTION).document(contact.id).delete().await()
    }

    override suspend fun acceptContactRequest(contact: Contact) {
        contact.careReceiverConnected = true
        firestore.collection(CONTACT_COLLECTION).document(contact.id).set(contact).await()
    }

    companion object {
        private const val CARE_RECEIVER_ID_FIELD = "careReceiverId"
        private const val CARE_GIVER_ID_FIELD = "careGiverId"
        private const val CONTACT_COLLECTION = "contacts"
    }
}
