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


@HiltViewModel
class ReminderViewModel @Inject constructor(
   private val repository: ReminderRepository
): ViewModel() {

    val reminders = repository.reminders

    fun onReminderCheckChange(reminder: Reminder) {
        viewModelScope.launch {
            repository.update(reminder.copy(completed = !reminder.completed))
        }
    }
    
//     private val _listofReminders: MutableState<List<Reminder>> = mutableStateOf(emptyList())
//     val listOfReminders: State<List<Reminder>> = _listofReminders
//     init{
//         viewModelScope.launch {
//             // trigger invoke fun
//             val reminderList = useCase()
//             // change the name later
//             _listofReminders.value = ReminderRepository.reminders
// //            reminderList.categories.forEach{
// //                Log.d("YW", it.strCategory)
// //                Log.d("YW", it.strCategoryDescription)
// //            }

//         }

//     }
}