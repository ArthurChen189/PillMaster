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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.utils.UserRole

@Composable
fun DashboardScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var errorText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Button(
            onClick = { navController.navigate(NavigationPath.LOGIN.route) }
        ) {
            Text("Login")
        }
        Button(
            onClick = {
                val user = User(
                    id = "123",
                    email = "example@example.com",
                    name = "John Doe",
                    password = "password",
                    roles = listOf(UserRole.ADMIN)
                )
                val userString = user.toString()
                navController.navigate("${NavigationPath.HOMEPAGE.route}/$userString")
            }
        ) {
            Text("Homepage Test")
        }
    }
}