package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ece452.pillmaster.viewmodel.HealthBotSearch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ece452.pillmaster.R
import kotlinx.coroutines.launch
import androidx.compose.material3.TopAppBar
import java.time.format.TextStyle
import androidx.compose.material3.MaterialTheme
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatSendMessage(
    healthBotSearch: HealthBotSearch = hiltViewModel()
) {
    /*
    This is a screen that allows the user to send a message to the health bot.
    User needs to send a message to query the health bot. The health bot will then
    respond with an answer. Note that each conversation is independent of each other,
    that is, the health bot will not remember the previous conversation.
    */

    // Create a coroutine scope that uses the main dispatcher.
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf(TextFieldValue("")) }

    TopAppBar(
        title = { Text("Chat with Health Bot") },
    )

    Column(
        // Use navigationBarsPadding() imePadding() and , to move the input panel above both the
        // navigation bar, and on-screen keyboard (IME)
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) {
        // Add a column to contain the text field and the response.
        Column {
            // Add a divider to separate the text field and the response.
            Divider(Modifier.height(0.2.dp))
            Box(
                // Add a box to contain the text field and the send button.
                Modifier
                    .padding(horizontal = 4.dp)
                    .padding(top = 6.dp, bottom = 10.dp)
            ) {
                // Add a text field to allow the user to send a message to the health bot.
                Row {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        label = null,
                        placeholder = { Text("Ask me anything", fontSize = 12.sp) },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                            .weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent),
                    )
                    IconButton(onClick = {
                        scope.launch {
                            val textClone = text.text
                            healthBotSearch.sendMessage(textClone)
                        }
                    }) {
                        Icon(
                            Icons.Filled.Send,
                            "sendMessage",
                            modifier = Modifier.size(26.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(4.dp)
            ) {
            Row(
                // Add a scrollable box to display the response if the text is too long.
                modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)

            ) {

                // Display the response from the health bot.

                if (healthBotSearch._messages.answer.isNotEmpty()) {
                    // Display the text content if it is not empty
                    Text(
                        text = healthBotSearch._messages.answer,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                } else {
                    // Display the note if there is no text content
                    Text(
                        text = "Hi! I am a Health Bot, you can ask me anything here!",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        color = Color.Gray // Use a different color for the note
                    )
                }
                }

            }
        }

    }
}

