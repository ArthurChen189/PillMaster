package com.ece452.pillmaster.screen.carereceiver

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.HealthBotSearch

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