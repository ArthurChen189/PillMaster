package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.CareGiverContactGroup
import com.ece452.pillmaster.model.CareReceiverContactGroup
import com.ece452.pillmaster.model.Contact
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val CARE_RECEIVER_ID_FIELD = "careReceiverId"
private const val CARE_GIVER_ID_FIELD = "careGiverId"
private const val CONTACT_COLLECTION = "contacts"

/**
 * Interface for the Contact Repository.
 * Provides methods to manage user contacts.
 */
interface IContactRepository {
    val sentContactRequests: Flow<List<Contact>>
    val pendingContactRequests: Flow<List<Contact>>
    val connectedContacts: Flow<List<Contact>>
    /**
     * Adds a new contact to the repository.
     *
     * @param currentUserId The unique identifier of the current user requesting to add the contact.
     * @param targetUserEmail The email address of the user to be added as a contact.
     * @throws Exception If attempting to add oneself as a contact or if an error occurs during the process.
     */
    suspend fun addContact(currentUserId: String, targetUserEmail: String)
    /**
     * Removes a contact from the repository.
     *
     * @param contact The Contact object representing the contact to be removed.
     * @throws Exception If an error occurs during the removal process.
     */
    suspend fun removeContact(contact: Contact)
    /**
     * Accepts a contact request and updates the contact's connection status in the repository.
     *
     * @param contact The Contact object representing the contact whose request is being accepted.
     * @throws Exception If an error occurs during the update process.
     */
    suspend fun acceptContactRequest(contact: Contact)
}

/**
 * Repository class for Care Receivers' contacts.
 * Implements the IContactRepository interface.
 */
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
}

class CareGiverContactRepository
@Inject constructor (
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository)
    : IContactRepository
{
    private var careReceiverContactGroup: CareReceiverContactGroup = CareReceiverContactGroup()

    override val sentContactRequests: Flow<List<Contact>>
        get() = careReceiverContactGroup.getSentContactRequests(getContacts())

    override val pendingContactRequests: Flow<List<Contact>>
        get() = careReceiverContactGroup.getPendingContactRequests(getContacts())

    override val connectedContacts: Flow<List<Contact>>
        get() = careReceiverContactGroup.getConnectedContacts(getContacts())

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getContacts(): Flow<List<Contact>> =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(CONTACT_COLLECTION)
                .whereEqualTo(CARE_GIVER_ID_FIELD, user.userId)
                .dataObjects()
        }

    override suspend fun addContact(currentUserId: String, targetUserEmail: String) {
        val currentUser = auth.getUserProfileById(currentUserId)
        val targetUser = auth.getUserProfileByEmail(targetUserEmail)
        if (currentUser.userId == targetUser.userId) {
            throw Exception("Cannot add yourself as a contact.")
        }
        val contact = careReceiverContactGroup.createContactRequest(currentUser = currentUser, targetUser = targetUser)
        contact.id = firestore.collection(CONTACT_COLLECTION).add(contact).await().id
    }

    override suspend fun removeContact(contact: Contact) {
        firestore.collection(CONTACT_COLLECTION).document(contact.id).delete().await()
    }

    override suspend fun acceptContactRequest(contact: Contact) {
        contact.careGiverConnected = true
        firestore.collection(CONTACT_COLLECTION).document(contact.id).set(contact).await()
    }
}

