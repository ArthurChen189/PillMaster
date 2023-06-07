package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun OnboardScreen(
    // TODO - Expose an action if this action takes the user to another screen.
    onNav: () -> Unit = {},
    tempReceiver: () -> Unit = {},
    tempGiver: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics { contentDescription = "Onboard Screen" }
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = onNav) {
            Text("Go to login")
        }
        // TODO - Notice that they should be removed when we have the authentication functionality.
        Button(onClick = tempReceiver) {
            Text("Go to receiver home")
        }
        Button(onClick = tempGiver) {
            Text("Go to giver home")
        }
    }
}