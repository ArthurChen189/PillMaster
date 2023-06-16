package com.ece452.pillmaster.usecase

import com.ece452.pillmaster.model.ReminderResponse
import com.ece452.pillmaster.repository.IReminderRepository
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