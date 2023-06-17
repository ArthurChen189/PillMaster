package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel
import io.github.boguszpawlowski.composecalendar.StaticCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.day.NonSelectableDayState
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    navController: NavController,
    vm: PillAddPageViewModel = hiltViewModel()
) {

    // Retrieve Medicine Data for Calendar View
    vm.fetchMedicineData()
    // Real-time Calendar
    // TODO - mock for now
    CustomizedCalendarView(medicineList = vm.testList.value)
//    CustomizedCalendarView(medicineList = vm.medicineList.value)

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomizedCalendarView(
    medicineList: List<Pair<String, Pair<String, String>>>
) {
    StaticCalendar(
        modifier = Modifier
            .animateContentSize()
            .fillMaxHeight(),
        dayContent = {
            DayContent(
                dayState = it,
                medicineList = medicineList,
            )
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DayContent(
    dayState: NonSelectableDayState,
    medicineList: List<Pair<String, Pair<String, String>>>
) {
    DefaultD(
        state = dayState,
        medicineList = medicineList
    )
}

fun shouldHighLight(list: List<Pair<String, Pair<String, String>>>, input: String): Boolean {
    return list.any { (_, innerPair) ->
        innerPair.first.contains(input) || innerPair.second.contains(input)
    }
}

fun findMedicineByInput(list: List<Pair<String, Pair<String, String>>>, input: String): List<String> {
    val result = mutableListOf<String>()
    for ((name, date) in list) {
        if (date.first.contains(input, ignoreCase = true)) {
            result.add("$name starts today")
        } else if (date.second.contains(input, ignoreCase = true)) {
            result.add("$name ends today")
        }
    }
    return result
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun <T : SelectionState> DefaultD(
    state: DayState<T>,
    currentDayColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (LocalDate) -> Unit = {},
    medicineList: List<Pair<String, Pair<String, String>>>
) {
    val date = state.date
    val selectionState = state.selectionState
    val cardData = remember { mutableStateListOf<String>() }
    var isCardClicked by remember { mutableStateOf(false) }
    // Decide if a day has events (i.e. take pill, end pill...)
    val isEventful = shouldHighLight(medicineList, date.toString())

   // Function to handle yellow card click
    val onYellowCardClick: () -> Unit = {
        // Update the state to indicate that the yellow card is clicked
        isCardClicked = true
        // Call findMedicineByInput to get the list of strings for the yellow card
        cardData.clear()
        cardData.addAll(findMedicineByInput(medicineList, date.toString()))
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp).clickable {
                onYellowCardClick()
                onClick(date)
                selectionState.onDateSelected(date)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = if (state.isCurrentDay) BorderStroke(1.dp, currentDayColor) else null,
        colors = CardDefaults.cardColors(
            contentColor = contentColorFor(
                backgroundColor = if (isEventful) Color.Yellow else MaterialTheme.colorScheme.surface
            ),
            containerColor = if (isEventful) Color.Yellow else MaterialTheme.colorScheme.surface
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

    if (isCardClicked && isEventful) {
        AlertDialog(
            onDismissRequest = {
                isCardClicked = false
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
}