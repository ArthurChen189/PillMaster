package com.ece452.pillmaster.screen.caregiver


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.BaseContactViewModel
import com.ece452.pillmaster.viewmodel.CareReceiverContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
inline fun<reified T : BaseContactViewModel> CareReceiverManageScreen(
    navController: NavController,
    contactViewModel: T = hiltViewModel()
) {
    val connectedContacts = contactViewModel.connectedContacts.collectAsStateWithLifecycle(emptyList())
    val sentContactRequests = contactViewModel.sentContactRequests.collectAsStateWithLifecycle(emptyList())
    val pendingContactRequests = contactViewModel.pendingContactRequests.collectAsStateWithLifecycle(emptyList())
    val contactUiState = contactViewModel.contactUiState
    val isCareReceiver = contactViewModel is CareReceiverContactViewModel
    val context = LocalContext.current

    if (contactUiState.error.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { contactViewModel.onErrorChange("") },
            title = { Text("Error") },
            text = { Text(contactUiState.error) },
            confirmButton = {
                Button(
                    onClick = { contactViewModel.onErrorChange("") }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),

        //horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Manage Carereceivers") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Divider(
            modifier = Modifier.padding(vertical = 20.dp),
            color = Color.Black
        )
        Text(text = "Connected Contacts", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        Box(
            modifier = Modifier
                .height(120.dp) // Fix the height of the Box to 200.dp
                .width(1000.dp)
                .background(color = Color.LightGray) // Set the background color of the Box to grey
                .padding(8.dp) // Add padding to create some space between the border and the content
        ) {
            LazyColumn() {
                items(connectedContacts.value) { connectedContactItem ->
//                val receiverId = if (isCareReceiver) connectedContactItem.careGiverId else connectedContactItem.careReceiverId
//                val receiverEmail = if (isCareReceiver) connectedContactItem.careGiverEmail else connectedContactItem.careReceiverEmail

                    ConnectedContactItem(
                        contact = connectedContactItem,
                        isCareReceiver = isCareReceiver,
                        onButtonClick = {
                            contactViewModel.onContactToRemoveChange(connectedContactItem)
                            contactViewModel.removeContact()
                        }
                    )
                }
            }
        }

        Text(text = "Sent Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        Box(
            modifier = Modifier
                .height(120.dp) // Fix the height of the Box to 200.dp
                .width(1000.dp)
                .background(color = Color.LightGray) // Set the background color of the Box to grey
                .padding(8.dp) // Add padding to create some space between the border and the content
        ) {
            LazyColumn() {
                items(sentContactRequests.value) { sentContactRequest ->
                    SentContactItem(
                        contact = sentContactRequest,
                        isCareReceiver = isCareReceiver,
                        onButtonClick = {
                            contactViewModel.onContactToRemoveChange(sentContactRequest)
                            contactViewModel.removeContact()
                        }
                    )
                }
            }
        }

        Text(text = "Pending Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        Box(
            modifier = Modifier
                .height(120.dp) // Fix the height of the Box to 200.dp
                .width(1000.dp)
                .background(color = Color.LightGray) // Set the background color of the Box to grey
                .padding(8.dp) // Add padding to create some space between the border and the content
        ) {
            LazyColumn() {
                items(pendingContactRequests.value) { pendingContactRequest ->
                    PendingContactItem(
                        contact = pendingContactRequest,
                        isCareReceiver = isCareReceiver,
                        onAcceptButtonClick = {
                            // Implement the action to accept the contact request
                            contactViewModel.onContactToAcceptChange(pendingContactRequest)
                            contactViewModel.acceptContact()
                        },
                        onRefuseButtonClick = {
                            // Implement the action to refuse the contact request
                            contactViewModel.onContactToRemoveChange(pendingContactRequest)
                            contactViewModel.removeContact()
                        }
                    )
                }
            }
        }
        Divider(
            modifier = Modifier.padding(vertical = 20.dp),
            color = Color.Black
        )
        Row() {
            TextField(
                value = contactUiState.newContactEmail,
                onValueChange = { contactViewModel.onNewContactEmailChange(it) },
                label = { Text("Add new contact") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                //.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (contactUiState.newContactEmail.isNotEmpty()) {
                        contactViewModel.addNewContact()
                    } else {
                        Toast.makeText(context, "Please fill in the user email", Toast.LENGTH_SHORT).show()
                    }
                },

                modifier = Modifier
                    .size(width = 200.dp, height = 50.dp)
                    .padding(end = 5.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Add")
            }
        }
        Row() {}


    }
}

@Composable
fun ConnectedContactItem(
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
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = "Delete",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun SentContactItem(
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
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = "Recall",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun PendingContactItem(
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
            Text(
                text = if (isCareReceiver) contact.careGiverEmail else contact.careReceiverEmail,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onAcceptButtonClick,
                modifier = Modifier.height(50.dp)
            ) {
                Text(text = "Accept",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onRefuseButtonClick,
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = "Refuse",
                    fontSize = 16.sp
                )
            }
        }
    }
}
