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

abstract class BaseContactViewModel constructor(
    private val authRepository: AuthRepository,
    protected val contactRepository: IContactRepository
) : ViewModel() {
    var messageUiState by mutableStateOf(MessageUiState())
        protected set

    val currentUserId: String
        get() = authRepository.getUserId()

    val connectedContacts: Flow<List<Contact>> = contactRepository.connectedContacts
    val sentContactRequests: Flow<List<Contact>> = contactRepository.sentContactRequests
    val pendingContactRequests: Flow<List<Contact>> = contactRepository.pendingContactRequests

    // Shared functions for removing, and accepting contacts
    abstract fun addNewContact(): Job

    fun removeContact() = viewModelScope.launch {
        try {
            messageUiState.contactToRemove?.let {
                contactRepository.removeContact(it)
            }
        } catch (e: Exception) {
            messageUiState = messageUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }

    fun acceptContact() = viewModelScope.launch {
        try {
            messageUiState.contactToAccept?.let {
                contactRepository.acceptContactRequest(it)
            }
        } catch (e: Exception) {
            messageUiState = messageUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }

    fun onNewContactEmailChange(email: String) {
        messageUiState = messageUiState.copy(newContactEmail = email)
    }

    fun onContactToRemoveChange(contact: Contact?) {
        messageUiState = messageUiState.copy(contactToRemove = contact)
    }

    fun onContactToAcceptChange(contact: Contact?) {
        messageUiState = messageUiState.copy(contactToAccept = contact)
    }
}

@HiltViewModel
class CareGiverContactViewModel @Inject constructor(
    authRepository: AuthRepository,
    @Named("careGiverContact") contactRepository: IContactRepository
) : BaseContactViewModel(authRepository, contactRepository) {
    override fun addNewContact(): Job = viewModelScope.launch {
        try {
            val newContactEmail = messageUiState.newContactEmail

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
            contactRepository.addContact(currentUserId, messageUiState.newContactEmail)
        } catch (e: Exception) {
            messageUiState = messageUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }
}

@HiltViewModel
class CareReceiverContactViewModel @Inject constructor(
    authRepository: AuthRepository,
    @Named("careReceiverContact") contactRepository: IContactRepository
): BaseContactViewModel(authRepository, contactRepository) {
    override fun addNewContact() = viewModelScope.launch {
        try {
            val newContactEmail = messageUiState.newContactEmail

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
            contactRepository.addContact(currentUserId, messageUiState.newContactEmail)
        } catch (e: Exception) {
            messageUiState = messageUiState.copy(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }
}

data class MessageUiState(
    var newContactEmail: String = "",
    var contactToRemove: Contact? = null,
    var contactToAccept: Contact? = null,
    var error: String? = null
)