package com.ece452.medicinesmartreminder.Reminder.repository

import com.ece452.medicinesmartreminder.Reminder.model.ReminderResponse
import com.ece452.medicinesmartreminder.Reminder.service.IReminderService
import javax.inject.Inject

interface IReminderRepository {
    suspend fun getAllReminders(): ReminderResponse
}

class ReminderRepository @Inject constructor(
    val service: IReminderService

) : IReminderRepository {
    override suspend fun getAllReminders(): ReminderResponse {
        return service.getAllReminders()
    }

}