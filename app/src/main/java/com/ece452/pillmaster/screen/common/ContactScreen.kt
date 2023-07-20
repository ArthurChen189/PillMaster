package com.ece452.pillmaster.screen.common

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.ece452.pillmaster.viewmodel.BaseContactViewModel
import com.ece452.pillmaster.viewmodel.CareReceiverContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
inline fun<reified T : BaseContactViewModel> ContactScreen(
    navController: NavController,
    messageViewModel: T = hiltViewModel()
) {
    val connectedContacts = messageViewModel.connectedContacts.collectAsStateWithLifecycle(emptyList())
    val sentContactRequests = messageViewModel.sentContactRequests.collectAsStateWithLifecycle(emptyList())
    val pendingContactRequests = messageViewModel.pendingContactRequests.collectAsStateWithLifecycle(emptyList())
    val messageUiState = messageViewModel.messageUiState
    val isCareReceiver = messageViewModel is CareReceiverContactViewModel
    val isError = messageUiState.error != null
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }

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
                    isCareReceiver = isCareReceiver,
                    onButtonClick = {
                        //
                    }
                )
            }
        }

        Text(text = "Sent Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sentContactRequests.value) { sentContactRequest ->
                SingleSentContactItem(
                    contact = sentContactRequest,
                    isCareReceiver = isCareReceiver,
                    onButtonClick = {
                        messageViewModel.onContactToRemoveChange(sentContactRequest)
                        messageViewModel.removeContact()
                    }
                )
            }
        }

        Text(text = "Pending Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(pendingContactRequests.value) { pendingContactRequest ->
                SinglePendingContactItem(
                    contact = pendingContactRequest,
                    isCareReceiver = isCareReceiver,
                    onAcceptButtonClick = {
                        // Implement the action to accept the contact request
                        messageViewModel.onContactToAcceptChange(pendingContactRequest)
                        messageViewModel.acceptContact()
                    },
                    onRefuseButtonClick = {
                        // Implement the action to refuse the contact request
                        messageViewModel.onContactToRemoveChange(pendingContactRequest)
                        messageViewModel.removeContact()
                    }
                )
            }
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
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text("Add new contact")
        }
    }
}

@Composable
fun SingleConnectedContactItem(
    contact: Contact,
    isCareReceiver: Boolean,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (isCareReceiver) contact.careGiverEmail else contact.careReceiverEmail,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.size(width = 100.dp, height = 50.dp)
            ) {
                Text("Chat")
            }
        }
    }
}

@Composable
fun SingleSentContactItem(
    contact: Contact,
    isCareReceiver: Boolean,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (isCareReceiver) contact.careGiverEmail else contact.careReceiverEmail,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.size(width = 100.dp, height = 50.dp)
            ) {
                Text("Recall")
            }
        }
    }
}

@Composable
fun SinglePendingContactItem(
    contact: Contact,
    isCareReceiver:Boolean,
    onAcceptButtonClick: () -> Unit,
    onRefuseButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = if (isCareReceiver) contact.careGiverEmail else contact.careReceiverEmail, fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onAcceptButtonClick,
                modifier = Modifier.size(width = 100.dp, height = 50.dp)
            ) {
                Text("Accept")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onRefuseButtonClick,
                modifier = Modifier.size(width = 100.dp, height = 50.dp)
            ) {
                Text("Refuse")
            }
        }
    }
}
