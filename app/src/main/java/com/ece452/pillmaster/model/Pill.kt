package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

// Pill data model
data class Pill(
    @DocumentId val id: String = "", // id of Pill
    var userId: String = "", // Carereceiver userId the Pill belongs to
    var name: String = "", // name of the Pill, generated from AddReminder
    var description: String = "", // description of Pill user enters, generated from AddReminder
    var info: String? = "Unknown", // detailed info, compatibility info of the Pill,
)
