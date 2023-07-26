package com.ece452.pillmaster.screen.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ece452.pillmaster.R
import com.ece452.pillmaster.utils.UserRole
import com.ece452.pillmaster.viewmodel.BaseUserChatViewModel
import com.ece452.pillmaster.viewmodel.CareGiverUserChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
inline fun<reified T : BaseUserChatViewModel> UserChatScreen(
    navController: NavController,
    receiverId: String,
    receiverEmail: String,
    userChatViewModel: T = hiltViewModel()
) {
    val chatHistory = userChatViewModel.chatHistory.collectAsStateWithLifecycle(emptyList())
    val chatUiState = userChatViewModel.chatUiState
    val currentUserId = userChatViewModel.currentUserId
    val role = if (userChatViewModel is CareGiverUserChatViewModel) {
        UserRole.CARE_GIVER
    } else {
        UserRole.CARE_RECEIVER
    }

    // Show a pop-up when there's an error
    if (chatUiState.error.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { userChatViewModel.onErrorChange("") },
            title = { Text("Error") },
            text = { Text(chatUiState.error) },
            confirmButton = {
                Button(
                    onClick = { userChatViewModel.onErrorChange("") }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TopAppBar
        TopAppBar(
            title = { Text("Chat Screen") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // Column for the chat screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Display chat history
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth()
            ) {
                items(chatHistory.value
                    .filter { chatMessage ->
                        (chatMessage.receiverId == receiverId && chatMessage.senderId == currentUserId)
                                || (chatMessage.senderId == receiverId && chatMessage.receiverId == currentUserId)
                    }
                ) { chatMessage ->
                    val senderLabel =
                        if (chatMessage.senderId == receiverId) receiverEmail else "Me"
                    val isSentByCurrentUser = chatMessage.senderId == currentUserId
                    // Apply different background colors and alignment based on sender
                    val backgroundColor = if (isSentByCurrentUser) Color.Gray else Color.White
                    val horizontalAlignment =
                        if (isSentByCurrentUser) Alignment.End else Alignment.Start

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color = backgroundColor),
                    ) {
                        Row(
                            modifier = Modifier

                                .padding(8.dp),
                            horizontalArrangement = if (isSentByCurrentUser) Arrangement.End else Arrangement.Start
                        ) {
                            // Optionally, you can add an avatar to identify the sender visually
                            if (!isSentByCurrentUser) {
                                // Replace the imageResource with your avatar image
                                Image(
                                    painter = painterResource(R.drawable.user_icon),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }else {
                                Image(
                                    painter = painterResource(R.drawable.user_icon_green),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$senderLabel (${chatMessage.timestamp})",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Display the message content
                                Text(
                                    text = chatMessage.message,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }


                    // Spacer to take up the remaining available space
                    Spacer(modifier = Modifier.weight(1f))

                    // Input field to type a new message
                    OutlinedTextField(
                        value = chatUiState.newMessage,
                        onValueChange = userChatViewModel::onNewMessageChange,
                        label = { Text("Type your message") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Send button to send the message
                    Button(
                        onClick = { userChatViewModel.sendMessage(receiverId = receiverId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "Send")
                    }
                }
            }
        }

