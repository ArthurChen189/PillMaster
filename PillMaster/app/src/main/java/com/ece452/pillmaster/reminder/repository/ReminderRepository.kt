package com.ece452.pillmaster.reminder.repository

import com.ece452.pillmaster.reminder.model.ReminderResponse
import com.ece452.pillmaster.reminder.service.IReminderService
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