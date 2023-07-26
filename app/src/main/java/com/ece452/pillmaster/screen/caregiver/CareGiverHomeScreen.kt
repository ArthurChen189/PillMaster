package com.ece452.pillmaster.screen.caregiver

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.ece452.pillmaster.screen.common.NavItem
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.BaseContactViewModel
import com.ece452.pillmaster.viewmodel.CareReceiverContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
inline fun<reified T : BaseContactViewModel> CareGiverHomeScreen(
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

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TODO - Build this screen as per the Figma file.
        TopAppBar(
            title = { Text("Contacts") },
//            navigationIcon = {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                }
//            }
        )
//        Text(text = "Contacts", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(connectedContacts.value) { connectedContactItem ->
                val receiverId = if (isCareReceiver) connectedContactItem.careGiverId else connectedContactItem.careReceiverId
                val receiverEmail = if (isCareReceiver) connectedContactItem.careGiverEmail else connectedContactItem.careReceiverEmail

                SingleConnectedContactItem(
                    contact = connectedContactItem,
                    isCareReceiver = isCareReceiver
                ) {
                    val route = if (isCareReceiver) {
                        NavigationPath.CARE_RECEIVER_USER_CHAT.route.replace("{receiverId}", receiverId).replace("{receiverEmail}", receiverEmail)
                    } else {
                        NavigationPath.CARE_GIVER_USER_CHAT.route.replace("{receiverId}", receiverId).replace("{receiverEmail}", receiverEmail)
                    }

                    navController.navigate(route)
                }
            }
        }

        NavBar(navController)
//        Text(text = "Sent Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
//        LazyColumn(modifier = Modifier.weight(1f)) {
//            items(sentContactRequests.value) { sentContactRequest ->
//                SingleSentContactItem(
//                    contact = sentContactRequest,
//                    isCareReceiver = isCareReceiver,
//                    onButtonClick = {
//                        contactViewModel.onContactToRemoveChange(sentContactRequest)
//                        contactViewModel.removeContact()
//                    }
//                )
//            }
//        }
//
//        Text(text = "Pending Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
//        LazyColumn(modifier = Modifier.weight(1f)) {
//            items(pendingContactRequests.value) { pendingContactRequest ->
//                SinglePendingContactItem(
//                    contact = pendingContactRequest,
//                    isCareReceiver = isCareReceiver,
//                    onAcceptButtonClick = {
//                        // Implement the action to accept the contact request
//                        contactViewModel.onContactToAcceptChange(pendingContactRequest)
//                        contactViewModel.acceptContact()
//                    },
//                    onRefuseButtonClick = {
//                        // Implement the action to refuse the contact request
//                        contactViewModel.onContactToRemoveChange(pendingContactRequest)
//                        contactViewModel.removeContact()
//                    }
//                )
//            }
//        }
//
//        TextField(
//            value = contactUiState.newContactEmail,
//            onValueChange = { contactViewModel.onNewContactEmailChange(it) },
//            label = { Text("Add new contact") },
//            shape = RoundedCornerShape(10.dp),
//            modifier = Modifier
//                .padding(10.dp)
//                .fillMaxWidth()
//        )
//
//        Button(
//            onClick = {
//                if (contactUiState.newContactEmail.isNotEmpty()) {
//                    contactViewModel.addNewContact()
//                } else {
//                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
//                }
//            },
//            modifier = Modifier.size(width = 200.dp, height = 50.dp)
//                .padding(bottom = 10.dp)
//        ) {
//            Text("Add new contact")
//        }
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
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = "Chat",
                    fontSize = 16.sp
                )
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
//        NavItem(Icons.Rounded.DateRange, "Calender", Color(0xFF227EBA)) {
//            navController.navigate(NavigationPath.CALENDAR.route)
//        }
//        NavItem(Icons.Rounded.Email, "Contact", Color(0xFF227EBA)) {
//            navController.navigate(NavigationPath.CARE_RECEIVER_CONTACT.route)
//        }
//        NavItem(Icons.Rounded.Face, "ChatBot", Color(0xFF227EBA)) {
//            navController.navigate(NavigationPath.HEALTH_BOT_PATH.route)
//        }
        NavItem(Icons.Rounded.Settings,"Setting", Color(0xFF227EBA) ) {
            navController.navigate(NavigationPath.CAREGIVER_SETTING.route)
        }
    }
}
