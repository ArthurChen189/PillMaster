package com.ece452.medicinesmartreminder.Reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ece452.medicinesmartreminder.Reminder.viewmodel.ReminderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = hiltViewModel()
) {

    val reminders = getFakeData()
    Column {
        Text("Reminders:")
        Spacer(modifier = Modifier.height(30.dp))
        reminders.forEach{
            Text(text = it)
    }
    }
}

fun getFakeData(): List<String> {
return listOf(
    "PillA",
    "PillB"
)
}