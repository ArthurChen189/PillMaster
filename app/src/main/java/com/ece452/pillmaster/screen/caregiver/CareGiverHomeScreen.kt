package com.ece452.pillmaster.screen.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ece452.pillmaster.R
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.utils.NavigationPath

// Sample data of receivers
val receiverList = listOf(
    "CareReceiver A",
    "CareReceiver B",
    "CareReceiver C",
    "CareReceiver D",
    "CareReceiver E",
    "CareReceiver F",
    "CareReceiver G",
    "CareReceiver H",
    "CareReceiver I",
    "CareReceiver J",
    "CareReceiver K",
    "CareReceiver L",
    "CareReceiver A",
    "CareReceiver B",
    "CareReceiver C",
    "CareReceiver D",
    "CareReceiver E",
    "CareReceiver F",
    "CareReceiver G",
    "CareReceiver H",
    "CareReceiver I",
    "CareReceiver J",
    "CareReceiver K",
    "CareReceiver L",
    "CareReceiver A",
    "CareReceiver B",
    "CareReceiver C",
    "CareReceiver D",
    "CareReceiver E",
    "CareReceiver F",
    "CareReceiver G",
    "CareReceiver H",
    "CareReceiver I",
    "CareReceiver J",
    "CareReceiver K",
    "CareReceiver L",
    "CareReceiver A",
    "CareReceiver B",
    "CareReceiver C",
    "CareReceiver D",
    "CareReceiver E",
    "CareReceiver F",
    "CareReceiver G",
    "CareReceiver H",
    "CareReceiver I",
    "CareReceiver J",
    "CareReceiver K",
    "CareReceiver L"
)


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CareGiverHomeScreen(
    // TODO - Expose an action if this action takes the user to another screen.
    navController: NavController,
    /*messageViewModel: CareGiverMessageViewModel = hiltViewModel()*/
) {

/*    val connectedContacts = messageViewModel.connectedContacts.collectAsStateWithLifecycle(emptyList())
    val messageUiState = messageViewModel.messageUiState
    val isError = messageUiState.error != null*/
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics { contentDescription = "Care giver's home screen" }
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShortNavBar(navController)
        /*Text(text = "Connected Contacts", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier
            .weight(1f)
            .padding(16.dp)) {
            items(connectedContacts.value) {
                    connectedContactItem ->
                SingleReceiverItem(contact = connectedContactItem)
            }
        }
        Button(
            onClick = {
                      showDialog = true
            },
            Modifier
                .padding(horizontal = 5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                Modifier.size((40.dp)),
                )
        }
        ShortNavBar(navController)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add new contact...") },
                text = {
                    TextField(
                        value = messageUiState.newContactEmail,
                        onValueChange = { messageViewModel.onNewContactEmailChange(it) },
                        label = { Text("Enter care receiver's ID here...") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (messageUiState.newContactEmail.isNotEmpty()) {
                            messageViewModel.addNewContact()
                        } else {
                            Toast.makeText(context, "Please enter the care receiver's ID!", Toast.LENGTH_SHORT).show()
                        }
                        showDialog = false
                    }) {
                        Text("Send contact request")
                    }
                },
            )
        }
        if (isError) {
            Toast.makeText(context, messageUiState.error ?: "unknown error", Toast.LENGTH_SHORT).show()
            messageUiState.error = null
        }*/
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleReceiverItem(
    contact: Contact
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier =
        Modifier
            .padding(5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(10.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                painter = rememberImagePainter(R.drawable.user_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = contact.careReceiverEmail,
                fontSize = 24.sp,
                modifier =
                Modifier.
                padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun ShortNavBar(
    navController: NavController,
) {
    Row(
        Modifier
            .height(80.dp)
            .padding(0.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(Icons.Rounded.Email, "Contact", Color(0xFF227EBA)) {
            navController.navigate(NavigationPath.CARE_GIVER_CONTACT.route)
        }
        NavItem(Icons.Rounded.Settings,"Setting", Color(0xFF227EBA) ) {
            // TODO go to care giver's manage page + sign out.
//            navController.navigate(NavigationPath.RECEIVER_SETTING.route)
        }
    }
}
