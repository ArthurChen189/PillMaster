package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId var id: String = "",
    var userId: String = "",
    var email: String = ""
)