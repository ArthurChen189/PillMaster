package com.ece452.pillmaster.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.IContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Abstract ViewModel class representing the base functionality for managing contacts.
 * @param authRepository The instance of the AuthRepository used for authentication operations.
 * @param contactRepository The instance of the IContactRepository for contact-related operations.
 */
abstract class BaseContactViewModel constructor(
    private val authRepository: AuthRepository,
    protected val contactRepository: IContactRepository
) : ViewModel() {
    var contactUiState by mutableStateOf(ContactUiState())
        protected set

    val currentUserId: String
        get() = authRepository.getUserId()

    val connectedContacts: Flow<List<Contact>> = contactRepository.connectedContacts
    val sentContactRequests: Flow<List<Contact>> = contactRepository.sentContactRequests
    val pendingContactRequests: Flow<List<Contact>> = contactRepository.pendingContactRequests

    // Shared functions for removing, and accepting contacts
    abstract fun addNewContact(): Job

    /**
     * Removes a contact from the user's contacts list.
     */
    fun removeContact() = viewModelScope.launch {
        try {
            contactUiState.contactToRemove?.let {
                contactRepository.removeContact(it)
            }
        } catch (e: Exception) {
            contactUiState = contactUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }

    /**
     * Accepts a contact request.
     */
    fun acceptContact() = viewModelScope.launch {
        try {
            contactUiState.contactToAccept?.let {
                contactRepository.acceptContactRequest(it)
            }
        } catch (e: Exception) {
            contactUiState = contactUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }

    /**
     * Updates the values in the contact UI state.
     */
    fun onNewContactEmailChange(email: String) {
        contactUiState = contactUiState.copy(newContactEmail = email)
    }

    fun onContactToRemoveChange(contact: Contact?) {
        contactUiState = contactUiState.copy(contactToRemove = contact)
    }

    fun onContactToAcceptChange(contact: Contact?) {
        contactUiState = contactUiState.copy(contactToAccept = contact)
    }

    fun onErrorChange(error: String) {
        contactUiState = contactUiState.copy(error = error)
    }
}

/**
 * ViewModel class for managing caregiver contacts.
 * @param authRepository The instance of the AuthRepository used for authentication operations.
 * @param contactRepository The instance of the IContactRepository for caregiver contact operations.
 */
@HiltViewModel
class CareGiverContactViewModel @Inject constructor(
    authRepository: AuthRepository,
    @Named("careGiverContact") contactRepository: IContactRepository
) : BaseContactViewModel(authRepository, contactRepository) {
    override fun addNewContact(): Job = viewModelScope.launch {
        try {
            val newContactEmail = contactUiState.newContactEmail

            // Check if the new contact email already exists in connectedContacts
            val connectedContactExists = contactRepository.connectedContacts.first().any { contact ->
                contact.careReceiverEmail == newContactEmail
            }
            if (connectedContactExists) {
                throw Exception("Contact with email $newContactEmail already exists in connected contacts.")
            }

            // Check if the new contact email already exists in sentContactRequests
            val sentContactRequestExists = contactRepository.sentContactRequests.first().any { contact ->
                contact.careReceiverEmail == newContactEmail
            }
            if (sentContactRequestExists) {
                throw Exception("Contact with email $newContactEmail already exists in sent contact requests.")
            }

            // Check if the new contact email already exists in pendingContactRequests
            val pendingContactRequestExists = contactRepository.pendingContactRequests.first().any { contact ->
                contact.careReceiverEmail == newContactEmail
            }
            if (pendingContactRequestExists) {
                throw Exception("Contact with email $newContactEmail already exists in pending contact requests.")
            }

            // Add the new contact if it doesn't exist in any of the lists
            contactRepository.addContact(currentUserId, contactUiState.newContactEmail)
        } catch (e: Exception) {
            contactUiState = contactUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }
}

/**
 * ViewModel class for managing care receiver contacts.
 * @param authRepository The instance of the AuthRepository used for authentication operations.
 * @param contactRepository The instance of the IContactRepository for care receiver contact operations.
 */
@HiltViewModel
class CareReceiverContactViewModel @Inject constructor(
    authRepository: AuthRepository,
    @Named("careReceiverContact") contactRepository: IContactRepository
): BaseContactViewModel(authRepository, contactRepository) {
    override fun addNewContact() = viewModelScope.launch {
        try {
            val newContactEmail = contactUiState.newContactEmail

            // Check if the new contact email already exists in connectedContacts
            val connectedContactExists = contactRepository.connectedContacts.first().any { contact ->
                contact.careGiverEmail == newContactEmail
            }
            if (connectedContactExists) {
                throw Exception("Contact with email $newContactEmail already exists in connected contacts.")
            }

            // Check if the new contact email already exists in sentContactRequests
            val sentContactRequestExists = contactRepository.sentContactRequests.first().any { contact ->
                contact.careGiverEmail == newContactEmail
            }
            if (sentContactRequestExists) {
                throw Exception("Contact with email $newContactEmail already exists in sent contact requests.")
            }

            // Check if the new contact email already exists in pendingContactRequests
            val pendingContactRequestExists = contactRepository.pendingContactRequests.first().any { contact ->
                contact.careGiverEmail == newContactEmail
            }
            if (pendingContactRequestExists) {
                throw Exception("Contact with email $newContactEmail already exists in pending contact requests.")
            }

            // Add the new contact if it doesn't exist in any of the lists
            contactRepository.addContact(currentUserId, contactUiState.newContactEmail)
        } catch (e: Exception) {
            contactUiState = contactUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }
}

/**
 * Data class representing the state of the contact-related UI elements.
 * @param newContactEmail The email of the new contact being added.
 * @param contactToRemove The contact to be removed.
 * @param contactToAccept The contact to be accepted.
 * @param error The error message to be displayed.
 */
data class ContactUiState(
    var newContactEmail: String = "",
    var contactToRemove: Contact? = null,
    var contactToAccept: Contact? = null,
    var error: String = ""
)