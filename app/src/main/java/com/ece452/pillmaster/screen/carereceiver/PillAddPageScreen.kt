package com.ece452.pillmaster.screen.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ece452.pillmaster.di.FirebaseModule
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.ReminderRepository
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillAddPageScreen(
    navController: NavController,
    entry: NavBackStackEntry,
    viewModel: PillAddPageViewModel = hiltViewModel()
) {
    var pillName by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("None") }
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
   var description by remember { mutableStateOf(savedStateHandle?.get<String>("description") ?: "") }

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


        TimePicker(
            reminderTime = reminderTime,
            onReminderTimeChange = { reminderTime = it },
            reminderTimeTitle = "*Reminder Time"
        )

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
//                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Link this reminder to your selected CareGiver above")
        }

        Button(
            onClick = {
                if (pillName.isNotEmpty() &&
                    description.isNotEmpty() &&
                    startDate.isNotEmpty()
                ) {
                    // All required fields are filled
                    // submit the input data here
                    viewModel.newPillSubmit(pillName,description, reminderTime, startDate, endDate, selectedOption, isChecked)
                    navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route)
                } else {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    date: String,
    onDateChange: (String) -> Unit,
    dateTitle: String
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

@Composable
fun TimePicker(
    reminderTime: String,
    onReminderTimeChange: (String) -> Unit,
    reminderTimeTitle: String
){
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            onReminderTimeChange("$mHour:$mMinute")
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



