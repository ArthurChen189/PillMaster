package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.day.NonSelectableDayState
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import java.time.LocalDate
import androidx.lifecycle.compose.collectAsStateWithLifecycle


var result: Any = 0

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    navController: NavController,
    vm: PillAddPageViewModel = hiltViewModel()
) {


    // Fetch Medicine Data
    val reminders = vm.reminders.collectAsStateWithLifecycle(emptyList())
    val pill_list = reminders.value
    // Process
    result = processMedicineData(pill_list)

    // Retrieve Medicine Data for Calendar View
    // Real-time Calendar
    CustomizedCalendarView(vm)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomizedCalendarView(
    vm: PillAddPageViewModel,
) {
    StaticCalendar(
        modifier = Modifier
            .animateContentSize()
            .fillMaxHeight(),
        dayContent = {
            DayContent(
                dayState = it,
                vm = vm,
            )
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DayContent(
    dayState: NonSelectableDayState,
    vm: PillAddPageViewModel,
) {
    DefaultD(
        state = dayState,
        vm = vm,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun processMedicineData(
    pillList: List<Reminder>
): Any {
    // Return immediately as there are no pills, even in the future.
    if (pillList.isEmpty()) {
        return 2
    }
    // Here we must find all pills eligible for the current date.
    val currentDate = LocalDate.now()
    val currentPills: List<Reminder> = pillList.filter { pill ->
        val startDate = LocalDate.parse(pill.startDate)
        val endDate = if (pill.endDate.isNotEmpty()) LocalDate.parse(pill.endDate) else LocalDate.MAX

        currentDate.isEqual(startDate) || currentDate.isEqual(endDate) ||
                (currentDate.isAfter(startDate) && (endDate == LocalDate.MAX || currentDate.isBefore(endDate)))
    }


    // Return now, if there are no pills for today.
    if (currentPills.isEmpty()) {
        return 2
    }

    val allMedicineTaken = currentPills.all { it.completed }
    val noMedicineTaken = currentPills.none { it.completed }

    return when {
        noMedicineTaken -> -1
        allMedicineTaken -> 1
        else -> {
            // Create sentences as prompts to user.
            val result = mutableListOf<String>()
            for (pill in currentPills) {
                val medicine = pill.name
                val isTaken = pill.completed
                val sentence = if (isTaken) {
                    "$medicine has been taken."
                } else {
                    "$medicine has not been taken."
                }
                result.add(sentence)
            }
            result
        }
    }
}

// This function is used when user clicks on a future date.
@RequiresApi(Build.VERSION_CODES.O)
fun processMedicineData(
    pillList: List<Reminder>,
    date: LocalDate // Add date parameter
): Any {
    // Return immediately as there are no pills, even in the future.
    if (pillList.isEmpty()) {
        return listOf("No medicines to take!")
    }
    // Here we must find all pills eligible for this future date.
    val currentPills: List<Reminder> = pillList.filter { pill ->
        val startDate = LocalDate.parse(pill.startDate)
        val endDate = if (pill.endDate.isNotEmpty()) LocalDate.parse(pill.endDate) else LocalDate.MAX

        date.isEqual(startDate) || date.isEqual(endDate) ||
                (date.isAfter(startDate) && (endDate == LocalDate.MAX || date.isBefore(endDate)))
    }

    // Return now, if there are no pills for today.
    // Tell user that on that specific future date, no medicines are to be taken.
    if (currentPills.isEmpty()) {
        return listOf("No medicines to take!")
    }

    // Create list of medicine names
    val medicineNames = currentPills.map { pill -> "• ${pill.name}" }
    return medicineNames
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun <T : SelectionState> DefaultD(
    state: DayState<T>,
    currentDayColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (LocalDate) -> Unit = {},
    vm: PillAddPageViewModel,
) {
    val date = state.date
    val selectionState = state.selectionState
    val cardData = remember { mutableStateListOf<String>() }
    var isCardClicked by remember { mutableStateOf(false) }
    var isFutureCardClicked by remember { mutableStateOf(false) }
    val futureCardData = remember { mutableStateListOf<String>() }

    // Function to handle yellow card (i.e. current date) click
    val onTodayClick: (vm: PillAddPageViewModel) -> Unit = {
        isCardClicked = true
        when (result) {
            is Int -> {
                // Handle the case when none or all medicines are taken
                if (result == -1) {
                    cardData.add("None of the medicines have been taken!")
                } else if (result == 1) {
                    cardData.add("All medicines have been taken!")
                } else if (result == 2) {
                    cardData.add("No medicines to take today!")
                }
            }
            is List<*> -> {
                cardData.addAll(result as Collection<String>)
            }
        }
    }

    val pill_list = vm.reminders.collectAsStateWithLifecycle(emptyList()).value

    val onFutureClick: () -> Unit = {
        isFutureCardClicked = true
        val futurePills = processMedicineData(pill_list, date) as List<String>
        futureCardData.addAll(futurePills)
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clickable {
                // Handle user clicking on the current date here.
                if (date == LocalDate.now()) {
                    onTodayClick(vm)
                }
                // Handle user clicking on a future date here.
                if (date > LocalDate.now()) {
                    onFutureClick()
                }
                onClick(date)
                selectionState.onDateSelected(date)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = if (state.isCurrentDay) BorderStroke(1.dp, currentDayColor) else null,
        // Logic for displaying different colors.
        colors = CardDefaults.cardColors(
            contentColor = contentColorFor(
                backgroundColor = if (result == -1 && date == LocalDate.now()) Color.Red
                else if (result == 1 && date == LocalDate.now()) Color.Green
                else if (date == LocalDate.now() && result != 2) Color.Yellow
                else MaterialTheme.colorScheme.surface
            ),
            containerColor = if (result == -1 && date == LocalDate.now()) Color.Red
            else if (result == 1 && date == LocalDate.now()) Color.Green
            else if (date == LocalDate.now() && result != 2) Color.Yellow
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = date.dayOfMonth.toString(),
            )
        }
    }

    if (isCardClicked) {
        AlertDialog(
            onDismissRequest = {
                isCardClicked = false
                // Must reset the cardData.
                cardData.clear()
            },
            title = { Text(text = "$date") },
            text = {
                // Show the list of strings for the yellow card
                Column {
                    cardData.forEach { item ->
                        Text(text = item)
                    }
                }
            },
            confirmButton = {
                // Close the dialog with a confirm button
                TextButton(
                    onClick = {
                        isCardClicked = false
                        cardData.clear()
                    }
                ) {
                    Text(text = "Got it!")
                }
            }
        )
    }

    if (isFutureCardClicked) {
        AlertDialog(
            onDismissRequest = {
                isFutureCardClicked = false
                // Must reset the futureCardData.
                futureCardData.clear()
            },
            title = { Text(text = "$date") },
            text = {
                if (futureCardData.isNotEmpty() && futureCardData[0] == "No medicines to take!") {
                    futureCardData.forEach { item ->
                        Text(text = item)
                    }
                } else {
                    Column {
                        Text(text = "Medicine(s) to take...", style = MaterialTheme.typography.titleMedium)
                        LazyColumn {
                            items(futureCardData) {
                                    item ->
                                Text(text = item)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                // Close the dialog with a confirm button
                TextButton(
                    onClick = {
                        isFutureCardClicked = false
                        futureCardData.clear()
                    }
                ) {
                    Text(text = "Got it!")
                }
            }
        )
    }
}