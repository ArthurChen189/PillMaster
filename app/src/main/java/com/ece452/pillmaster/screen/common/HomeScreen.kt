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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.AuthResult
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.LoginViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun HomeScreen(
    navController: NavController,
    testUser: Boolean,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var userRoles by remember { mutableStateOf(emptyList<String>()) }
    var errorText by remember { mutableStateOf("") }

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

        // Call the getUserMetadata function to fetch user metadata
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        val result = if (testUser) {
            val rolesJson =
                """
                    {
                        "roles": [
                            "care_giver",
                            "care_receiver"
                        ]
                    }
                """.trimIndent()
            AuthResult.Success(Gson().fromJson(rolesJson, object : TypeToken<Map<String, Any>>() {}.type))
        } else {
            loginViewModel.getUserMetadata()
        }

        when (result) {
            is AuthResult.Success -> {
                val metadata: MutableMap<String, Any> = result.data.toMutableMap()
                val roles = metadata["roles"] as List<String>

                // Update the userRole variable with the fetched role
                userRoles = roles

                // Display the appropriate content based on the user role
                if (roles.contains("care_receiver")) {
                    // Display content for care receiver
                    Button(onClick = { navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route) }) {
                        Text(text = "Go to Care Receiver Home")
                    }
                } else {
                    Button(onClick = {
                        val updatedRoles = roles.toMutableList()
                        updatedRoles.add("care_receiver")
                        metadata["roles"] = updatedRoles

                        val updateResult = loginViewModel.setUserMetadata(metadata)
                        if (updateResult is AuthResult.Success) {
                            userRoles = updatedRoles
                        } else if (updateResult is AuthResult.Failure) {
                            val errorMessage: String = updateResult.errorMessage
                        }
                    }) {
                        Text(text = "Assign Care Receiver Role")
                    }
                }

                if (roles.contains("care_giver")) {
                    // Display content for care receiver
                    Button(onClick = { navController.navigate(NavigationPath.CARE_GIVER_HOMEPAGE.route) }) {
                        Text(text = "Go to Care Giver Home")
                    }
                } else {
                    Button(onClick = {
                        val updatedRoles = roles.toMutableList()
                        updatedRoles.add("care_giver")
                        metadata["roles"] = updatedRoles

                        val updateResult = loginViewModel.setUserMetadata(metadata)
                        if (updateResult is AuthResult.Success) {
                            userRoles = updatedRoles
                        } else if (updateResult is AuthResult.Failure) {
                            val errorMessage: String = updateResult.errorMessage
                        }
                    }) {
                        Text(text = "Assign Care Giver Role")
                    }
                }
            }
            is AuthResult.Failure -> {
                errorText = result.errorMessage
            }
        }
    }
}
