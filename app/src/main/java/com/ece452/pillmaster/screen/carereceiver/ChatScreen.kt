package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    // TODO - Expose an action if this action takes the user to another screen.
    navController: NavController,
//    viewModel: HealthBotSearch = hiltViewModel(),
// this viewModel is injected into ChatSendMessage.
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(12),
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                ChatSendMessage()
            }
        }
    }
}