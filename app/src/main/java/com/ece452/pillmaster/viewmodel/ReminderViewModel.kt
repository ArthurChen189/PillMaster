package com.ece452.pillmaster.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.ReminderRepository
import com.ece452.pillmaster.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

// View model for Care receiver homepage for viewing and managing reminder list
@HiltViewModel
class ReminderViewModel @Inject constructor(
   private val repository: ReminderRepository
): ViewModel() {

    // Reminder list of the user
    val reminders = repository.reminders

    // When user toggles the checkbox for the reminder row 
    fun onReminderCheckChange(reminder: Reminder) {
        viewModelScope.launch {
            repository.update(reminder.copy(completed = !reminder.completed))
        }
    }

    // User Read Mode: text to speech
    fun buildReminderText(reminderList: List<Reminder>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Today you need to")
        for (reminder in reminderList) {
            if (!reminder.completed){
                stringBuilder.append("take ${reminder.name} at Time ${reminder.time},")
            }
        }
        return stringBuilder.toString()
    }
}