package com.ece452.pillmaster.screen.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ece452.pillmaster.model.ReminderTime
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

// The screen for adding a new pill reminder
@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillAddPageScreen(
    navController: NavController,
    entry: NavBackStackEntry,
    viewModel: PillAddPageViewModel = hiltViewModel()
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // Saved info of the new pill reminder
    var pillName by remember { mutableStateOf(savedStateHandle?.get<String>("pillName") ?: "")  }
    var reminderTime by remember {mutableStateOf(savedStateHandle?.get<String>("reminderTime") ?: "")}
    var startDate by remember { mutableStateOf(savedStateHandle?.get<String>("startDate") ?: "")}
    var endDate by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("None") }
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var description by remember { mutableStateOf(savedStateHandle?.get<String>("description") ?: "") }
    val reminderTimeList = remember { mutableStateListOf<ReminderTime>() }

    if (reminderTime != "") {
        val hour = reminderTime.substringBefore(":").toInt()
        val min = reminderTime.substringAfter(":").toInt()
        reminderTimeList.clear()
        reminderTimeList.add(ReminderTime(hour = hour, min = min, timeString = reminderTime))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        TextField(
            value = pillName,
            onValueChange = { pillName = it },
            label = { Text("*Pill Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("*Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    // auto fill
                    navController.navigate(NavigationPath.CAMERA_HOMEPAGE.route)
                },
            ){
                Text(text = "scan your prescription to auto fill")
            }
        }

        // Allow user to add multiple reminder times,
        // when user clicks submit, newPillSubmit to reminder repository for each reminderTime
        Column(){
            Text("Selected Reminder Times:", fontSize = 20.sp)
            Spacer(modifier = Modifier.size(5.dp))
            LazyRow {
                items(reminderTimeList) {reminderTime  ->
                    Row(
                        modifier = Modifier.width(150.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(reminderTime.timeString, fontSize = 25.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = {reminderTimeList.remove(reminderTime)},
                            shape = RoundedCornerShape(10.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete",
                                )
                        }
                    }
                }
            }

            // Time picker that allows user to add multiple reminder times
            TimePicker2(
                selectedTimes = reminderTimeList,
                onSelectedTimesChange = { times ->
                    reminderTimeList.clear()
                    reminderTimeList.addAll(times)
                }

            )
        }

        DatePicker(
            date = startDate,
            onDateChange = { startDate = it },
            dateTitle = "*Start Date"
        )

        DatePicker(
            date = endDate,
            onDateChange = { endDate = it },
            dateTitle = "End Date"
        )

        CareGiverDropdownMenu(
            selectedText = selectedOption,
            onSelectedTextChange = { selectedOption = it },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Link this reminder to your selected CareGiver above")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route)
                },
                modifier = Modifier.size(width = 100.dp, height = 50.dp)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Add the new pill reminder
            Button(
                onClick = {
                    if (pillName.isNotEmpty() &&
                        description.isNotEmpty() &&
                        startDate.isNotEmpty()
                    ) {
                        // All required fields are filled
                        // submit the input data here
                        viewModel.pillListSubmit(
                            pillName,
                            description,
                            reminderTimeList,
                            startDate,
                            endDate,
                            selectedOption,
                            isChecked
                        )
                        navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route)
                    } else {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(width = 100.dp, height = 50.dp)
            ) {
                Text("Submit")
            }

        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGiverDropdownMenu(
    selectedText: String,
    onSelectedTextChange: (String) -> Unit,
) {
    val careGivers = arrayOf("PERSON1", "PERSON2", "PERSON3", "None")  //getCareGivers()
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },

        ) {
            TextField(
                value = if(selectedText == "None"){
                    "Select a CareGiver (Optional)"
                }else{
                    selectedText
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                careGivers.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onSelectedTextChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DatePicker(
    date: String,
    onDateChange: (String) -> Unit,
    dateTitle: String,
    viewModel: PillAddPageViewModel = hiltViewModel()
){
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val selectedDate = LocalDate.of(mYear, mMonth + 1, mDayOfMonth)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            viewModel.month = mMonth
            viewModel.year = mYear
            viewModel.day = mDayOfMonth
            onDateChange(selectedDate.format(formatter).toString())
        }, mYear, mMonth, mDay
    )

    Button(
        onClick = {
        mDatePickerDialog.show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .size(55.dp),
        shape = CutCornerShape(0.dp),
    )
     {
        Text(text = "$dateTitle $date", fontSize = 20.sp)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TimePicker(
    reminderTime: String,
    onReminderTimeChange: (String) -> Unit,
    reminderTimeTitle: String,
    viewModel: PillAddPageViewModel = hiltViewModel()
){
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            onReminderTimeChange("$mHour:$mMinute")
            viewModel.hour = mHour
            viewModel.minute = mMinute
        }, mHour, mMinute, false
    )

    Button(
        onClick = {
            mTimePickerDialog.show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .size(55.dp),
        shape = CutCornerShape(0.dp),
        )
    {
        Text(text = "$reminderTimeTitle $reminderTime", fontSize = 20.sp)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TimePicker2(
    selectedTimes: List<ReminderTime>,
    onSelectedTimesChange: (List<ReminderTime>) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerDialog = remember {
        TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            val updatedTimes = selectedTimes.toMutableList()
            updatedTimes.add(ReminderTime(hour = selectedHour, min = selectedMinute, timeString = formattedTime))
            onSelectedTimesChange(updatedTimes)
        }
    }

    Button(
        onClick = {
            TimePickerDialog(
                context,
                timePickerDialog,
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                false
            ).show()
        },
        modifier = Modifier
            .height(40.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add",
            Modifier.size((30.dp)),

            )
    }
}







