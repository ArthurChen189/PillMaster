package com.ece452.pillmaster.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Structural Design Pattern: Composite Design Pattern
 * The ContactComponent interface acts as the component that defines the common operations
 * for both individual contacts and contact groups.
 */
interface ContactComponent {
    fun createContactRequest(currentUser: User, targetUser: User): Contact
    fun addContact(contact: Contact, list: MutableList<Contact>): MutableList<Contact>
    fun removeContact(contact: Contact, list: MutableList<Contact>): MutableList<Contact>
    fun updateContact(contact: Contact, list: MutableList<Contact>): MutableList<Contact>
    fun getSentContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>>
    fun getPendingContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>>
    fun getConnectedContacts(list: Flow<List<Contact>>): Flow<List<Contact>>
}

abstract class ContactGroup()
    : ContactComponent
{
    abstract override fun createContactRequest(currentUser: User, targetUser: User): Contact
    abstract override fun getSentContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>>
    abstract override fun getPendingContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>>

    override fun addContact(
        contact: Contact,
        list: MutableList<Contact>
    ) : MutableList<Contact> {
        if (!list.contains(contact)) {
            list.add(contact)
        }
        return list
    }

    override fun removeContact(
        contact: Contact,
        list: MutableList<Contact>
    ): MutableList<Contact> {
        val index = list.indexOfFirst { it.id == contact.id }
        if (index != -1) {
            list.removeAt(index)
        }
        return list
    }

    override fun updateContact(
        contact: Contact,
        list: MutableList<Contact>
    ): MutableList<Contact> {
        val index = list.indexOfFirst { it.id == contact.id }
        if (index != -1) {
            list[index] = contact
        }
        return list
    }

    override fun getConnectedContacts(
        list: Flow<List<Contact>>
    ): Flow<List<Contact>> {
        return list.map { contacts ->
            contacts.filter { contact ->
                contact.careReceiverConnected && contact.careGiverConnected
            }
        }
    }
}

/**
 * Behavioural Design Pattern: Template Method Design Pattern
 * The ContactGroup class is an abstract class that defines the common structure and algorithm.
 */
class CareReceiverContactGroup : ContactGroup()
{
    override fun createContactRequest(currentUser: User, targetUser: User): Contact {
        return Contact(
            careReceiverId = targetUser.userId,
            careReceiverEmail = targetUser.email,
            careReceiverConnected = false,
            careGiverId = currentUser.userId,
            careGiverEmail = currentUser.email,
            careGiverConnected = true
        )
    }

    override fun getSentContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>> {
        return list.map { contacts ->
            contacts.filter { contact ->
                !contact.careReceiverConnected && contact.careGiverConnected
            }
        }
    }

    override fun getPendingContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>> {
        return list.map { contacts ->
            contacts.filter { contact ->
                contact.careReceiverConnected && !contact.careGiverConnected
            }
        }
    }
}

class CareGiverContactGroup: ContactGroup() {
    override fun createContactRequest(currentUser: User, targetUser: User): Contact {
        return Contact(
            careReceiverId = currentUser.userId,
            careReceiverEmail = currentUser.email,
            careReceiverConnected = true,
            careGiverId = targetUser.userId,
            careGiverEmail = targetUser.email,
            careGiverConnected = false
        )
    }

    override fun getSentContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>> {
        return list.map { contacts ->
            contacts.filter { contact ->
                contact.careReceiverConnected && !contact.careGiverConnected
            }
        }
    }

    override fun getPendingContactRequests(list: Flow<List<Contact>>): Flow<List<Contact>> {
        return list.map { contacts ->
            contacts.filter { contact ->
                !contact.careReceiverConnected && contact.careGiverConnected
            }
        }
    }
}