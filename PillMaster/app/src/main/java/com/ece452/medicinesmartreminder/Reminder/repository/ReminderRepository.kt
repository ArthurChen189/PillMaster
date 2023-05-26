package com.ece452.medicinesmartreminder.Reminder.repository

import javax.inject.Inject

interface IReminderRepository {
    fun getAllReminders(): String
}

class ReminderRepository @Inject constructor(

) : IReminderRepository {
    override fun getAllReminders(): String {
        return "Reminder Repo"
    }

}