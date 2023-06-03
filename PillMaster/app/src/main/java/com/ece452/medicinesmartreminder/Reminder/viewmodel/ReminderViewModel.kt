package com.ece452.medicinesmartreminder.Reminder.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.medicinesmartreminder.Reminder.model.Category
import com.ece452.medicinesmartreminder.Reminder.usecase.IGetReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReminderViewModel @Inject constructor(
   useCase: IGetReminderUseCase
): ViewModel() {

    private val _listofReminders: MutableState<List<Category>> = mutableStateOf(emptyList())
    val listOfReminders: State<List<Category>> = _listofReminders
    init{
        viewModelScope.launch {
            // trigger invoke fun
            val reminderList = useCase()
            // change the name later
            _listofReminders.value = reminderList.categories
//            reminderList.categories.forEach{
//                Log.d("YW", it.strCategory)
//                Log.d("YW", it.strCategoryDescription)
//            }

        }

    }
}