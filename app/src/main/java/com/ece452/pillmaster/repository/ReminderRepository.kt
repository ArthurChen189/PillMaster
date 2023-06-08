package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.ReminderResponse
import com.ece452.pillmaster.service.IReminderService
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