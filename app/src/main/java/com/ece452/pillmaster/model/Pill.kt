package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

data class Pill(
    @DocumentId var id: String = "", // id of Pill
    var name: String = "", // name of the Pill user enters
    var description: String = "", // description of Pill user enters, including dosage
    var info: String = "TODO", // detailed info, compatibility info of the Pill, TODO: Retrieve from Cloud Function
)