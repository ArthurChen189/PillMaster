package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

data class Contact (
    @DocumentId var id: String = "",
    val careReceiverId: String = "",
    val careReceiverEmail: String = "",
    var careReceiverConnected: Boolean = false,
    val careGiverId: String = "",
    val careGiverEmail: String = "",
    var careGiverConnected: Boolean = false,
)