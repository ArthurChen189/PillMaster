package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

data class Reminder(
    @DocumentId var id: String = "", // id of Reminder
    var userId: String = "", // Carereceiver userId the Reminder belongs to
    var name: String = "", // name of the Pill user enters
    var description: String = "", // description of Reminder user enters, including dosage
    var time1: String = "", // reminder times user enters, the max is 6 times per day
    var time2: String = "",
    var time3: String = "",
    var time4: String = "",
    var time5: String = "",
    var time6: String = "",
    var startDate: String = "", // start date of Reminder user enters
    var endDate: String = "", // end date of Reminder user enters, could be null
    var giverId: String = "", // userId of Caregiver user link the Reminder to, could be null
    var send2Giver: Boolean = false, // send Reminder info to Caregiver or not, false if null
    var completed: Boolean = false, // if Reminder is completed, false if null
)