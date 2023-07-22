package com.ece452.pillmaster.screen.carereceiver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.model.Pill
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.MessageViewModel
import com.ece452.pillmaster.viewmodel.PillManagementViewModel

@Composable
fun CaregiverManageScreen(
    navController: NavController,
    viewModel: MessageViewModel = hiltViewModel(),
    ){
    val connectedContacts = viewModel.connectedContacts.collectAsStateWithLifecycle(emptyList())
    val showDialog = remember { mutableStateOf(false) }
    val contact = remember { mutableStateOf(Contact())}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(modifier = Modifier.weight(1f)){
            val connectedContactsList = connectedContacts.value
            items(connectedContactsList) { connectedContactsItem ->
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF85CED4)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(80.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        contact.value = connectedContactsItem
                        showDialog.value = true
                    }) {
                    Text(text = connectedContactsItem.careGiverId, fontSize = 30.sp)
                }
            }
        }
        if (showDialog.value) {
            CaregiverDescriptionPopup(
                contact = contact.value,
                onClose = { showDialog.value = false },
                onDeleteChange = { viewModel.onContactToRemoveChange(contact.value)}
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),

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
fun CaregiverDescriptionPopup(
    contact: Contact,
    onClose: () -> Unit,
    onDeleteChange: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp),
        onDismissRequest = { onClose() },
        title = { Text(text = contact.careGiverId) },
        text = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Email:")
                Text(text = contact.careGiverId, fontWeight = FontWeight.Bold)

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