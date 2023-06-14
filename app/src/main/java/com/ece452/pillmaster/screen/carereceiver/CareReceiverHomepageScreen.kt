package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.NavigationPath

// Sample data of medicines
val medicines = listOf(
    "Medicine A",
    "Medicine B",
    "Medicine C",
    "Medicine D",
    "Medicine E",
    "Medicine F",
    "Medicine G",
    "Medicine H",
    "Medicine I",
    "Medicine J",
    "Medicine K",
    "Medicine L",
    "Medicine A",
    "Medicine B",
    "Medicine C",
    "Medicine D",
    "Medicine E",
    "Medicine F",
    "Medicine G",
    "Medicine H",
    "Medicine I",
    "Medicine J",
    "Medicine K",
    "Medicine L"
)

@Composable
fun CareReceiverHomepageScreen(
    // TODO - Expose an action if this action takes the user to another screen.
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp) // Adjust the value as needed for spacing
                .semantics { contentDescription = "Care receiver's home screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO - Build this screen as per the Figma file.
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(medicines) { medicine ->
                    Text(text = medicine, modifier = Modifier.padding(8.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = {
                // transfer to pillAddPageScreen
//                navController.navigate(
//                    NavigationPath.CARE_RECEIVER_HOMEPAGE.route)
                //println("Button clicked!")
                navController.navigate(NavigationPath.PILL_ADD_PAGE.route)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                //tint = MaterialTheme.
            )
        }


    }
}


