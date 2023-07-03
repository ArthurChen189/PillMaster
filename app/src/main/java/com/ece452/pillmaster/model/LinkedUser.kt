package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

data class LinkedUser(
    @DocumentId var id: String = "", // document id
    val linkedUserId: String = "", // userId of linkedUser
    val linkedUserEmail: String = "", // email of linkedUser
)