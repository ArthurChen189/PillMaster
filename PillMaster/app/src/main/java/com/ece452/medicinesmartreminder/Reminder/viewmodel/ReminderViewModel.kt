package com.ece452.medicinesmartreminder.Reminder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ece452.medicinesmartreminder.Reminder.IGetReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ReminderViewModel @Inject constructor(

   useCase: IGetReminderUseCase
): ViewModel() {
    init{
        // trigger invoke fun
        val a = useCase()
        Log.d("YW", a)
    }
}