package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.LoginViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    userId: String?,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var errorText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (userId != null) {
            // Display content for care giver
            Button(onClick = { navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route) }) {
                Text(text = "Care Receiver Homepage")
            }

            // Display content for care receiver
            Button(onClick = { navController.navigate(NavigationPath.CARE_GIVER_HOMEPAGE.route) }) {
                Text(text = "Care Giver Homepage")
            }

            Button(onClick = {
                loginViewModel.signoutUser()
                navController.navigate(NavigationPath.DASHBOARD.route)
            }) {
                Text(text = "Sign out")
            }
        } else {
            errorText = "Invalid User."
        }
    }
}
