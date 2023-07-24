package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

// User data model
data class User (
    @DocumentId var id: String = "", // firestore document id of User
    var userId: String = "", // firebase auth userId
    var email: String = "", // email of User for sign up, login, search for Caregiver/Care receiver
)