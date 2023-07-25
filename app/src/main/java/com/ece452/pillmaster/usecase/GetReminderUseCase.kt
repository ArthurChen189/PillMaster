package com.ece452.pillmaster.usecase

import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

interface  IGetReminderUseCase{

   suspend operator fun invoke(): List<Reminder>

}

class GetReminderUseCase @Inject constructor(
    val repo: ReminderRepository
): IGetReminderUseCase {
    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
    override suspend fun invoke(): List<Reminder> {
        return repo.reminders.flattenToList()

    }

}