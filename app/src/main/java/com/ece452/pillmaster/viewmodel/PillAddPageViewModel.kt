package com.ece452.pillmaster.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.di.FirebaseModule
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.ReminderRepository
import kotlinx.coroutines.launch

// TODO consider renaming this so as to serve for both uploading and retrieving
// TODO pill info to and from the database.
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PillAddPageViewModel @Inject constructor(private val repository: ReminderRepository) : ViewModel() {

    val reminders = repository.reminders


    // submit a new pill
    fun newPillSubmit(
        pillName: (String),
        direction: (String),
        reminderTime: (String),
        startDate: (String),
        endDate: (String),
        selectedOption: (String),
        isChecked: (Boolean)) {
        val reminder = Reminder()
        reminder.name = pillName
        reminder.description = direction
        reminder.time = reminderTime
        reminder.startDate = startDate
        reminder.endDate = endDate
        reminder.giverId = selectedOption
        reminder.send2Giver = isChecked
        viewModelScope.launch {
            repository.save(reminder)
        }
    }

    // get all Caregivers of current Carereceiver
    // return a List<String>
    suspend fun getCareGivers() {

    }

    // Below are for PillEditPageViewModel (Implement if needed)
    // val reminder = mutableStateOf(Reminder())

    // fun onNameChange(newValue: String) {
    //     reminder.value = reminder.value.copy(name = newValue)
    // }

    // fun onDescriptionChange(newValue: String) {
    //     reminder.value = reminder.value.copy(description = newValue)
    // }

    // fun onTimeChange(hour: Int, minute: Int) {
    //     val newTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
    //     reminder.value = reminder.value.copy(time = newTime)
    // }

    // fun onStartDateChange(newValue: Long) {
    //     val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
    //     calendar.timeInMillis = newValue
    //     val newStartDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
    //     reminder.value = reminder.value.copy(startDate = newStartDate)
    // }

    // fun onEndDateChange(newValue: Long) {
    //     val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
    //     calendar.timeInMillis = newValue
    //     val newEndDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
    //     reminder.value = reminder.value.copy(endDate = newEndDate)
    // }

    // fun onGiverIdChange(newValue: String) {
    //     reminder.value = reminder.value.copy(giverId = newValue)
    // }

    // fun onSend2GiverToggle(newValue: Boolean) {
    //     reminder.value = reminder.value.copy(send2Giver = newValue)
    // }

    // fun onDoneClick(popUpScreen: () -> Unit) {
    //     repository.save(reminder.value)
    //     popUpScreen()
    // }

    // private fun Int.toClockPattern(): String {
    //     return if (this < 10) "0$this" else "$this"
    // }

    // companion object {
    //     private const val UTC = "UTC"
    //     private const val DATE_FORMAT = "EEE, d MMM yyyy"
    // }

}