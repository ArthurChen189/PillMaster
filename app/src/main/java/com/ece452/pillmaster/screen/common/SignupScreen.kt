package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.LoginViewModel
import com.ece452.pillmaster.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState = loginViewModel.loginUiState
    val isError = loginUiState.signUpError != null
    val context = LocalContext.current

    // Top bar with back button to Home Screen
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sign Up",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        if (isError) {
            Text(
                text = loginUiState.signUpError ?: "unknown error",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        OutlinedTextField(
            value = loginUiState.userNameSignUp,
            onValueChange = { loginViewModel.onUserNameSignUpChange(it) },
            label = { Text("Email") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            isError = isError,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        OutlinedTextField(
            value = loginUiState.passwordSignUp,
            onValueChange = { loginViewModel.onPasswordSignUpChange(it) },
            label = { Text("Password") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            isError = isError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        OutlinedTextField(
            value = loginUiState.confirmPasswordSignUp,
            onValueChange = { loginViewModel.onConfirmPasswordSignUpChange(it) },
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            isError = isError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Checkbox for users to accept the privacy policy
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Checkbox(
                checked = loginUiState.policyAccepted,
                modifier = Modifier.padding(6.dp),
                onCheckedChange = { loginViewModel.onPolicyToggled(it) },
            )

            Text("You have to agree to our ")

            val clickableText = buildAnnotatedString{append("Privacy Policy")}
            ClickableText(text = clickableText, style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
            ), onClick = {navController.navigate(NavigationPath.POLICY.route)})
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.9F),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
        ){
            Text(
                " before signing up.",
            )
        }

        Button(
            onClick = { loginViewModel.createUser(context) },
            enabled = loginUiState.policyAccepted, // Sign up button is disabled until user accepts the privacy policy
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Already have an Account?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = { navController.navigate(NavigationPath.LOGIN.route) }) {
            Text(text = "Log In")
        }

        if (loginUiState.isLoading == true) {
            CircularProgressIndicator()
        }
    }
}