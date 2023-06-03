package com.ece452.pillmaster.reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.ece452.pillmaster.reminder.model.Category
import com.ece452.pillmaster.reminder.viewmodel.ReminderViewModel
import coil.compose.rememberImagePainter

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = hiltViewModel()
) {
// when the state in viewModel changes, we will get updates
    val listOfReminders by remember{ viewModel.listOfReminders}
//    val reminders = getFakeData()
    Column {
        Spacer(modifier = Modifier.height(30.dp))
        Text("Today's Reminders:", fontSize = 30.sp)
//        reminders.forEach{
//            Text(text = it)
//    }
        LazyColumn {
            items(listOfReminders){ item ->
               SingleReminderItem(item)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleReminderItem (
    reminder: Category
){
    Card(  shape = RoundedCornerShape(10.dp), modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth(),
    ) {
        Row (modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically){

            Image( modifier =  Modifier.size(40.dp),
                painter = rememberImagePainter(reminder.strCategoryThumb),
                contentDescription = null
            )

            Text(text = "Pill Name: " + reminder.strCategory, fontSize = 24.sp)


            Checkbox(
                // below line we are setting
                // the state of checkbox.
               checked = false,
                // below line is use to add padding
                // to our checkbox.
                modifier = Modifier.padding(16.dp),
                // below line is use to add on check
                // change to our checkbox.
                onCheckedChange = {  },
            )
        }
    }

}

//fun getFakeData(): List<String> {
//return listOf(
//    "PillA",
//    "PillB"
//)
//}