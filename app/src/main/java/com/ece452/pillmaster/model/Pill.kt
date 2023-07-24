package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

// Pill data model
data class Pill(
    @DocumentId val id: String = "", // firestore document id of Pill
    var userId: String = "", // Care receiver userId the Pill belongs to
    var name: String = "", // name of the Pill, generated from Add Reminder
    var description: String = "", // description of Pill user enters, generated from Add Reminder
    var info: String = "", // detailed info, compatibility info of the Pill, retrieved from Cloud Function
)
