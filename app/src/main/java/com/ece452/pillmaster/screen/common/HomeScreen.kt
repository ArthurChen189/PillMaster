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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.utils.UserRole
import com.ece452.pillmaster.viewmodel.LoginViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    user: User?
) {
    val errorText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
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

        if (user != null) {
            if (user.roles.contains(UserRole.CARE_RECEIVER) || user.roles.contains(UserRole.ADMIN)) {
                // Display content for care receiver
                Button(onClick = { navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route) }) {
                    Text(text = "Go to Care Receiver Home")
                }
            } else {
                Button(onClick = {
                    // TODO: ASSIGN ROLE
                }) {
                    Text(text = "Assign Care Receiver Role")
                }
            }

            if (user.roles.contains(UserRole.CARE_GIVER) || user.roles.contains(UserRole.ADMIN)) {
                // Display content for care receiver
                Button(onClick = { navController.navigate(NavigationPath.CARE_GIVER_HOMEPAGE.route) }) {
                    Text(text = "Go to Care Giver Home")
                }
            } else {
                Button(onClick = {
                    // TODO: ASSIGN ROLE
                }) {
                    Text(text = "Assign Care Giver Role")
                }
            }

            Button(onClick = {
                loginViewModel.signoutUser()
                navController.navigate(NavigationPath.DASHBOARD.route)
            }) {
                Text(text = "Sign out")
            }
        } else {
            // TODO
        }
    }
}
