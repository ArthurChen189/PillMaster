package com.ece452.pillmaster.screen.carereceiver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.NavigationPath

@Composable
fun CaregiverManageScreen(
    navController: NavController,

    ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {
            Button(
                onClick = {
                    navController.navigate(NavigationPath.RECEIVER_SETTING.route)
                },
                modifier = Modifier.size(width = 120.dp, height = 50.dp)

                //.padding(start = 16.dp)
            ) {
                Text("Back")
            }


        }
    }
}