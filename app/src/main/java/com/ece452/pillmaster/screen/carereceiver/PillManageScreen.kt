package com.ece452.pillmaster.screen.carereceiver


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Pill
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.PillManagementViewModel

@Composable
fun PillManageScreen(
    navController: NavController,
    viewModel: PillManagementViewModel = hiltViewModel(),
){
    val pills = viewModel.pills.collectAsStateWithLifecycle(emptyList())
    val showDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(modifier = Modifier.weight(1f)){
            val pillList = pills.value
            items(pillList) { pillItem ->
                // TODO requested by Anna: show reminderTime below each SingleReminderItem's reminderName
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(80.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        viewModel.onGetPill(pillItem.id)
                        showDialog.value = true
                }) {
                    Text(text = pillItem.name, fontSize = 30.sp)
                }
            }
        }
        if (showDialog.value) {
            ItemDescriptionPopup(
                pill = viewModel.pill.value,
                onClose = { showDialog.value = false },
                onDeleteChange = { viewModel.onPillDelete(viewModel.pill.value.id)}
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)

        ) {
            Button(

                onClick = {
                    navController.navigate(NavigationPath.RECEIVER_SETTING.route)
                },
                modifier = Modifier.size(width = 120.dp, height = 50.dp)
            ) {
                Text("Back")
            }


        }
    }
}



@Composable
fun ItemDescriptionPopup(
    pill: Pill,
    onClose: () -> Unit,
    onDeleteChange: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .size(500.dp)
            .padding(16.dp),
        onDismissRequest = { onClose() },
        title = { Text(text = pill.name) },
        text = {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    item {
                        Text(text = "Description:")
                        Text(text = pill.description, fontWeight = FontWeight.Bold)
                    }
                    item {
                        Text(text = "Info:")
                        Text(text = pill.info, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },

        confirmButton = {
            Row(
                Modifier.padding(top = 8.dp),
                
                ) {
                Button(
                    onClick = { onClose() },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Close")
                }
                Spacer(modifier = Modifier.weight(1f))
                // Todo need to delete not only the pill in database, also those reminders relating this pill
                Button(
                    onClick = {
                        onDeleteChange()
                        onClose()
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    )
}