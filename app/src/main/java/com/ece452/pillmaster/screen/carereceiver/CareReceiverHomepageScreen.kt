package com.ece452.pillmaster.screen.common

import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ece452.pillmaster.R
import com.ece452.pillmaster.model.Reminder
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel
import com.ece452.pillmaster.viewmodel.ReminderViewModel
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CareReceiverHomepageScreen(
    // TODO - Expose an action if this action takes the user to another screen.
    navController: NavController,
    vm: PillAddPageViewModel = hiltViewModel(),
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    val reminders = viewModel.reminders.collectAsStateWithLifecycle(emptyList())
    var reminderText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(bottom = 0.dp) // Adjust the value as needed for spacing
            .semantics { contentDescription = "Care receiver's home screen" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TODO - Build this screen as per the Figma file.
        LazyColumn(modifier = Modifier.weight(1f)) {
            val reminderList = reminders.value
            val today = LocalDate.now()
            val todayReminderList: List<Reminder> = reminderList.filter { reminder ->
                val startDate = LocalDate.parse(reminder.startDate)
                val endDate =
                    if (reminder.endDate.isNotEmpty()) LocalDate.parse(reminder.endDate) else LocalDate.MAX
                today.isEqual(startDate) || today.isEqual(endDate) ||
                        (today.isAfter(startDate) && (endDate == LocalDate.MAX || today.isBefore(
                            endDate
                        )))
            }
            val todayReminderListSorted = todayReminderList.sortedBy { reminder ->
                val time = reminder.time.split(":")
                time[0].toInt() * 60 + time[1].toInt()
            }

            reminderText = viewModel.buildReminderText(todayReminderListSorted)
            items(todayReminderListSorted) { reminderItem ->
                // TODO requested by Anna: show reminderTime below each SingleReminderItem's reminderName
                SingleReminderItem(
                    reminder = reminderItem,
                ) { viewModel.onReminderCheckChange(reminderItem) }
            }
        }
        AddPillButton(navController, reminderText)
        NavBar(navController)
    }
}

@Composable
fun NavBar(
    navController: NavController,
) {
    Row(
        Modifier
            .height(80.dp)
            .padding(0.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(Icons.Rounded.DateRange, "Calender", Color(0xFF227EBA)) {
            navController.navigate(NavigationPath.CALENDAR.route)
        }
        NavItem(Icons.Rounded.Email, "Message", Color(0xFF227EBA)) {
            navController.navigate(NavigationPath.MESSAGE.route)
        }
        NavItem(Icons.Rounded.Face, "ChatBot", Color(0xFF227EBA)) {
            navController.navigate(NavigationPath.HEALTH_BOT_PATH.route)
        }
        NavItem(Icons.Rounded.Settings,"Setting", Color(0xFF227EBA) ) {
            navController.navigate(NavigationPath.RECEIVER_SETTING.route)
        }
    }
}

@Composable
fun RowScope.NavItem(
    icon: ImageVector, description: String,
    tint: Color,
    navigateTo: () -> Unit = {},
) {
    Button(
        onClick = navigateTo,
        Modifier
            .weight(1f)
            .fillMaxHeight(),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        Icon(
            icon, description,
            Modifier
                .size(40.dp)
                .weight(1f),
            tint = tint
        )
    }

}

@Composable
fun AddPillButton(
    navController: NavController,
    reminderText: String
) {
    val context = LocalContext.current
    var isSpeaking by remember { mutableStateOf(false) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    //TextToSpeech init
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                }
            }
        }
        onDispose {
            tts?.shutdown()
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp),
    ) {
        Button(
            onClick = {
                navController.navigate(NavigationPath.PILL_ADD_PAGE.route)
            },
            Modifier
                .padding(horizontal = 5.dp)
                .weight(3.5f),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                Modifier.size((40.dp)),

                )
        }

        //read/pause all reminders
        Button(
            onClick = {
                if (isSpeaking) {
                    tts?.stop()
                } else {
                    tts?.setSpeechRate(0.5f)
                    tts?.speak(reminderText, TextToSpeech.QUEUE_ADD, null, null)
                }
                isSpeaking = !isSpeaking
            },
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .weight(1.5f),
            shape = RoundedCornerShape(10.dp)
        ) {
            val speakerIcon: Painter = painterResource(id = R.drawable.ic_speaker)
            Icon(
                painter = speakerIcon,
                contentDescription = "Speaker",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


// please use under code to implement each pill reminder
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleReminderItem(
    reminder: Reminder,
    onCheckChange: () -> Unit,
) {

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(10.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                modifier = Modifier.size(50.dp),
                painter = rememberImagePainter(R.drawable.reminder_capture),
                contentDescription = null
            )

            Text(text = reminder.name, fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.weight(1f))

            Checkbox(
                // below line we are setting
                // the state of checkbox.
                checked = reminder.completed,
                // below line is use to add padding
                // to our checkbox.
                modifier = Modifier.padding(16.dp),
                // below line is use to add on check
                // change to our checkbox.
                onCheckedChange = { onCheckChange() },
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "    Pill Time: " + reminder.time,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}


