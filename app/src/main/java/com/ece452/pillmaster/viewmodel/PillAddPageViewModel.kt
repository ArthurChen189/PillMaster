package com.ece452.pillmaster.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.repository.ReminderRepository
import com.ece452.pillmaster.broadcast.ReminderReceiver
import com.ece452.pillmaster.PillMasterApplication
import com.ece452.pillmaster.model.ReminderTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// View model for PillAddPage for adding a new pill reminder
// TODO consider renaming this so as to serve for both uploading and retrieving
// TODO pill info to and from the database.
@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class PillAddPageViewModel @Inject constructor(
    private val application: PillMasterApplication,
    private val repository: ReminderRepository) : ViewModel() {

    // Reminder list of the user
    val reminders = repository.reminders


    data class Pill(
        val name: String,
        val isTaken: Boolean,
        val startDate: String,
        val endDate: String,
        // Add more fields as needed
    )

    // Static list for testing purpose.
    // This is already replaced by reminder list of the user
    val testList = mutableStateOf(mutableListOf<Pill>())
    init {
        testList.value = mutableListOf(
            Pill("Aspirin", false, "2023-06-17", "2023-06-30"),
            Pill("Vitamin D", false, "2023-06-20", "2023-06-25"),
            Pill("Antibiotics", false, "2023-06-11", "2023-06-20"),
        )
        sortPillsByDate(testList.value)
    }

    var month = 0
    var day = 0
    var year = 0

    var hour = 0
    var minute = 0

    @RequiresApi(Build.VERSION_CODES.S)
    fun sortPillsByDate(testList: MutableList<Pill>) {
        val currentDate = LocalDate.now()

        // Partition the pills into currently-taking and future pills
        val (currentPills, futurePills) = testList.partition { pill ->
            val startDate = LocalDate.parse(pill.startDate)
            val endDate = LocalDate.parse(pill.endDate)
            currentDate in startDate..endDate
        }

        // Clear the original list and add the current and future pills in the modified order
        testList.clear()
        testList.addAll(currentPills)
        testList.addAll(futurePills)
    }



    // submit multiple reminders for each reminder time
    fun pillListSubmit(
        pillName: (String),
        direction: (String),
        reminderTimeList: (List<ReminderTime>),
        startDate: (String),
        endDate: (String),
        selectedOption: (String),
        isChecked: (Boolean)
    ){
        for (reminderTime in reminderTimeList){
            newPillSubmit(pillName,
                direction,
                reminderTime,
                startDate,
                endDate,
                selectedOption,
                isChecked
            )
        }

    }

    // submit a single reminder
    @RequiresApi(Build.VERSION_CODES.S)
    fun newPillSubmit(
        pillName: (String),
        direction: (String),
        reminderTime: (ReminderTime),
        startDate: (String),
        endDate: (String),
        selectedOption: (String),
        isChecked: (Boolean)) {
        val reminder = Reminder()
        reminder.name = pillName
        reminder.description = direction
        reminder.time = reminderTime.timeString
        reminder.startDate = startDate
        reminder.endDate = endDate
        reminder.giverId = selectedOption
        reminder.send2Giver = isChecked
        viewModelScope.launch (Dispatchers.IO){

            val id = repository.save(reminder)
            Log.d("id",id)
            val cal : Calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.DAY_OF_MONTH, day)

            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, reminderTime.hour)
            cal.set(Calendar.MINUTE, reminderTime.min)
            id?.let {
                var res = it.replace("[^0-9]".toRegex(), "")
                scheduleNotification(cal,res.toLong() * Math.floor(Math.random() * 999).toLong(), pillName, reminderTime.hour, reminderTime.min)
            }
        }
    }

    private fun scheduleNotification(calender: Calendar, id: Long, pillName: String, hour:Int, minute:Int) {

        val reminderAlarmManager: AlarmManager = application.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(application.applicationContext, ReminderReceiver::class.java)

        intent.putExtra("INTENT_NOTIFY", true)

        intent.putExtra("id", id)
        intent.putExtra("title", "Pill Master")
        intent.putExtra("msg","Remember to take $pillName at $hour:$minute :)")
        val pandingIntent: PendingIntent =
            PendingIntent.getBroadcast(application.applicationContext,
                id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val differenceInMillis = System.currentTimeMillis()  - calender.timeInMillis
        val differenceInSeconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMillis)

        if (differenceInSeconds < 120 || calender.timeInMillis >= System.currentTimeMillis() ) {
            reminderAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,  calender.timeInMillis , pandingIntent)
        } else {
            reminderAlarmManager.cancel(pandingIntent)
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