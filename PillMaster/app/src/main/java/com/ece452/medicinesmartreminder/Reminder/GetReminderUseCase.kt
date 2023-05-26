package com.ece452.medicinesmartreminder.Reminder

import android.telephony.IccOpenLogicalChannelResponse
import com.ece452.medicinesmartreminder.Reminder.repository.IReminderRepository
import javax.inject.Inject

interface  IGetReminderUseCase{

    operator fun invoke(): String

}
class GetReminderUseCase @Inject constructor(
    val repo: IReminderRepository
): IGetReminderUseCase {
    override fun invoke(): String {
        return  repo.getAllReminders()

        TODO("Not yet implemented")
    }

}