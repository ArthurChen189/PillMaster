package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ece452.pillmaster.R
import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.utils.NavigationPath

@Composable
fun DashboardScreen(
    navController: NavController
) {
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
        Image(
            painter = painterResource(id = R.drawable.pill),
            contentDescription = "Pill Image",
            modifier = Modifier
                .size(150.dp)
        )
        Text(
            text = "Pill Master",
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 50.dp)
        )
        Button(
            onClick = { navController.navigate(NavigationPath.LOGIN.route) }
        ) {
            Text("Login")
        }
    }
}