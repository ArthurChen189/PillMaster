package com.ece452.pillmaster.screen.caregiver

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ece452.pillmaster.R
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.screen.carereceiver.SingleConnectedContactItem
import com.ece452.pillmaster.screen.common.NavBar
import com.ece452.pillmaster.viewmodel.CareGiverMessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CareGiverMessageScreen(
    navController: NavController,
    messageViewModel: CareGiverMessageViewModel = hiltViewModel()
) {
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
            Toast.makeText(context, messageUiState.error ?: "unknown error", Toast.LENGTH_SHORT).show()
            messageUiState.error = null
        }

        Text(text = "Sent Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sentContactRequests.value) { sentContactRequest ->
                SingleRequestItem(
                    contact = sentContactRequest,
                )
            }
        }

        Text(text = "Pending Contact Requests", fontSize = 24.sp, modifier = Modifier.padding(start = 10.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(pendingContactRequests.value) { pendingContactRequest ->
                SingleRequestItem(
                    contact = pendingContactRequest,
                )
            }
        }

        Button(
            onClick = {
                if (pendingContactRequests.value.isNotEmpty()) {
                    messageViewModel.onContactToAcceptChange(pendingContactRequests.value[0])
                    messageViewModel.acceptContact()
                    if (!isError) {
                        Toast.makeText(context, "Contact request accepted.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No contact to accept", Toast.LENGTH_SHORT).show()
                }
                // TODO: be able to select one contact to accept request
            },
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
        ) {
            Text("refuse first contact request")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleRequestItem(
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
