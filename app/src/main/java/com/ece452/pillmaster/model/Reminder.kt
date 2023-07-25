package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

// Reminder data model
// Store one Reminder data model for each reminder time, 
// so it's easy to show on homescreen and complete/uncomplete each Reminder row
data class Reminder(
    @DocumentId val id: String = "", // firestore document id of Reminder
    var userId: String = "", // Care receiver userId the Reminder belongs to
    var name: String = "", // name of the Pill user enters
    var description: String = "", // description of Reminder user enters or scans, including dosage
    var time: String = "", // reminder time user enters
    var startDate: String = "", // start date of Reminder user enters
    var endDate: String = "", // end date of Reminder user enters, could be null
    var giverId: String = "", // userId of Caregiver user link the Reminder to, could be null
    var send2Giver: Boolean = false, // send Reminder info to Caregiver or not, false if null
    var completed: Boolean = false, // if Reminder is completed, false if null
)

// Reminder time data model
data class ReminderTime(
    var hour: Int = 0, // hour of reminder time
    var min: Int = 0,  // minute of reminder time
    var timeString: String = "", // reminder time in string format
)