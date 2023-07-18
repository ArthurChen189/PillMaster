package com.ece452.pillmaster.screen.carereceiver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ece452.pillmaster.model.Pill
import com.ece452.pillmaster.utils.NavigationPath

@Composable
fun CaregiverManageScreen(
    navController: NavController,

    ){
    val showDialog = remember { mutableStateOf(false) }
    val userId = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(modifier = Modifier.weight(1f)){

//            items(pillList) { pillItem ->
//                Button(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .size(80.dp)
//                        .padding(10.dp),
//                    shape = RoundedCornerShape(10.dp),
//                    onClick = {
//
//                    }) {
//                    Text(text = "", fontSize = 30.sp)
//                }
//            }
        }
        if (showDialog.value) {
            FloatingWindow(
                userId = userId.value,
                onUserIdChange = { userId.value = it },
                onDismiss = { showDialog.value = false }
            )
        }
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
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    showDialog.value = true
                },
                Modifier.size(width = 120.dp, height = 50.dp),


                ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    Modifier.size((30.dp)),

                    )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingWindow(
    userId: String,
    onUserIdChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Text(text = "Enter Caregiver ID:", fontWeight = FontWeight.Bold)
        TextField(
            value = userId,
            onValueChange = onUserIdChange,
            modifier = Modifier.fillMaxWidth()
        )
        Row() {
            Button(
                onClick = {
                    onDismiss.invoke()
                },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    // Todo request by Scott: we need to add caregiver viewModel to help to read, submit and delete caregiver
                    // Handle submission logic here
                    // You can perform any necessary operations
                    // with the user ID and then dismiss the window.
                    onDismiss.invoke()
                },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text("Submit")
            }
        }


    }
}