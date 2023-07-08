package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.screen.common.NavBar
import com.ece452.pillmaster.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageScreen(
    navController: NavController,
    messageViewModel: MessageViewModel = hiltViewModel()
) {
    val connectedContacts = messageViewModel.connectedContacts.collectAsStateWithLifecycle(emptyList())
    val sentContactRequests = messageViewModel.sentContactRequests.collectAsStateWithLifecycle(emptyList())
    val pendingContactRequests = messageViewModel.pendingContactRequests.collectAsStateWithLifecycle(emptyList())
    val messageUiState = messageViewModel.messageUiState
    val isError = messageUiState.error != null
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(bottom = 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TODO - Build this screen as per the Figma file.
        if (isError) {
            Text(
                text = messageUiState.error ?: "unknown error",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Text(text = "Connected Contacts", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(connectedContacts.value) { connectedContactItem ->
                SingleConnectedContactItem(
                    contact = connectedContactItem,
                )
            }
        }

        Text(text = "Sent Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sentContactRequests.value) { sentContactRequest ->
                SingleConnectedContactItem(
                    contact = sentContactRequest,
                )
            }
        }

        Text(text = "Pending Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(pendingContactRequests.value) { pendingContactRequest ->
                SingleConnectedContactItem(
                    contact = pendingContactRequest,
                )
            }
        }

        Button(
            onClick = {
                if (pendingContactRequests.value.isNotEmpty()) {
                    messageViewModel.onContactToAcceptChange(pendingContactRequests.value[0])
                    messageViewModel.acceptContact()
                } else {
                    Toast.makeText(context, "No contact to accept", Toast.LENGTH_SHORT).show()
                }
                // TODO: be able to select one contact to accept request
            },
            modifier = Modifier.size(width = 100.dp, height = 50.dp)
        ) {
            Text("Accept first contact request")
        }

        Button(
            onClick = {
                if (pendingContactRequests.value.isNotEmpty()) {
                    messageViewModel.onContactToRemoveChange(pendingContactRequests.value[0])
                    messageViewModel.removeContact()
                } else {
                    Toast.makeText(context, "No contact to refuse", Toast.LENGTH_SHORT).show()
                }
                // TODO: be able to select one contact to accept request
            },
            modifier = Modifier.size(width = 100.dp, height = 50.dp)
        ) {
            Text("refuse first contact request")
        }

        TextField(
            value = messageUiState.newContactEmail,
            onValueChange = { messageViewModel.onNewContactEmailChange(it) },
            label = { Text("Add new contact") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (messageUiState.newContactEmail.isNotEmpty()) {
                    messageViewModel.addNewContact()
                } else {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.size(width = 100.dp, height = 50.dp)
        ) {
            Text("Add new contact")
        }

        NavBar(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleConnectedContactItem (
    contact: Contact
){
    Card(  shape = RoundedCornerShape(10.dp), modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),
    ) {
        Row (modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(text = contact.careGiverEmail, fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}
