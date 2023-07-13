package com.ece452.pillmaster.screen.carereceiver

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.LoginViewModel
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel

@Composable
fun ReceiverSettingScreen(
    navController: NavController,
    viewModel: PillAddPageViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                navController.navigate(NavigationPath.PILL_MANAGE.route)

            },
            modifier = Modifier
                .fillMaxWidth()
                .size(55.dp),
            shape = RoundedCornerShape(10.dp),
        )
        {
            Text(text = "Manage your pills", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                navController.navigate(NavigationPath.CAREGIVER_MANAGE.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(55.dp),
            shape = RoundedCornerShape(10.dp),
        )
        {
            Text(text = "Manage your caregivers", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route)
                },
                modifier = Modifier.size(width = 120.dp, height = 50.dp)

                //.padding(start = 16.dp)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {
                    loginViewModel.signoutUser()
                    navController.navigate(NavigationPath.DASHBOARD.route)
                },
                modifier = Modifier.size(width = 120.dp, height = 50.dp)
            ) {
                Text("Sign out")
            }

        }
    }

}
