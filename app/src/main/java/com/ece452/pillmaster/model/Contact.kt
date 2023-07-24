package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

/**
 * Data class representing a Contact entity.
 *
 * @param id: The unique identifier of the Contact.
 * @param careReceiverId: The unique identifier of the care receiver associated with this Contact.
 * @param careReceiverEmail: The email address of the care receiver associated with this Contact.
 * @param careReceiverConnected: A boolean flag indicating whether the care receiver is connected.
 *                               (true if connected, false if not connected)
 * @param careGiverId: The unique identifier of the care giver associated with this Contact.
 * @param careGiverEmail: The email address of the care giver associated with this Contact.
 * @param careGiverConnected: A boolean flag indicating whether the care giver is connected.
 *                            (true if connected, false if not connected)
 */
data class Contact (
    @DocumentId var id: String = "",
    val careReceiverId: String = "",
    val careReceiverEmail: String = "",
    var careReceiverConnected: Boolean = false,
    val careGiverId: String = "",
    val careGiverEmail: String = "",
    var careGiverConnected: Boolean = false,
)