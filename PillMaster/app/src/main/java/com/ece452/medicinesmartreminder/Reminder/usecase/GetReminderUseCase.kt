package com.ece452.medicinesmartreminder.Reminder.usecase

import com.ece452.medicinesmartreminder.Reminder.model.ReminderResponse
import com.ece452.medicinesmartreminder.Reminder.repository.IReminderRepository
import javax.inject.Inject

interface  IGetReminderUseCase{

   suspend operator fun invoke(): ReminderResponse

}
class GetReminderUseCase @Inject constructor(
    val repo: IReminderRepository
): IGetReminderUseCase {
    override suspend fun invoke(): ReminderResponse {
        return  repo.getAllReminders()

        TODO("Not yet implemented")
    }

}