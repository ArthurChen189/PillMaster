package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    navController: NavController,
) {
    /*
    This is a screen that entails the ChatSendMessage composable. This screen is used to
    send a message to the health bot. The health bot will then respond with an answer.
     */

     // Add a surface to contain the ChatSendMessage composable.
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(12),
    ) {
        // Add a column to contain the ChatSendMessage composable.
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                // Add the ChatSendMessage composable.
                ChatSendMessage()
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                ) {
                    Button(
                        // Add a button to navigate back to the previous screen.
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.size(width = 120.dp, height = 50.dp)
                    ) {
                        Text("Back")
                    }


                }
            }
        }
    }
}