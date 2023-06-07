package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ece452.pillmaster.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNav: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics { contentDescription = "Login Screen" }
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.padding(16.dp),
            label = { Text(text = "Email") }
        )

        TextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.padding(16.dp),
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Sign In")
        }

        OutlinedButton(
            onClick = onNav,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Back")
        }
    }
}