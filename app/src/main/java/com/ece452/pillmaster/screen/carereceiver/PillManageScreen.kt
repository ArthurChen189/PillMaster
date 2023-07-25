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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Pill
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.PillManagementViewModel

// On PillManagementScreen, user can click on each pill to view name, description and info
// Open pill info viewing screen to show the fields of pill
// On PillManagementScreen, show each pill's name and a button to delete the pill
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillManageScreen(
    navController: NavController,
    viewModel: PillManagementViewModel = hiltViewModel(),
){
    val pills = viewModel.pills.collectAsStateWithLifecycle(emptyList())
    val showDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
            //.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Manage Pills") },

            )
        LazyColumn(modifier = Modifier.weight(1f)){
            val pillList = pills.value
            items(pillList) { pillItem ->
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF85CED4)),

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
            PillDescriptionPopup(
                pill = viewModel.pill.value,
                onClose = { showDialog.value = false },
                onDeleteChange = { viewModel.onPillDelete(viewModel.pill.value.id)}
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            Button(

                onClick = {
                    navController.navigate(NavigationPath.RECEIVER_SETTING.route)
                },
                modifier = Modifier.size(width = 120.dp, height = 50.dp)
            ) {
                Text("Back")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    navController.navigate(NavigationPath.PILL_ADD_PAGE.route)
                },
                Modifier.size(width = 120.dp, height = 50.dp),


                ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    Modifier.size((30.dp)),

                    )
            }

        }
    }
}



@Composable
fun PillDescriptionPopup(
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
                        Text(text = "Potential drug-drug interactions:")
                        Text(text = pill.info.toString(), fontWeight =  FontWeight.Bold)
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