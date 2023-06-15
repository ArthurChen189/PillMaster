package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(bottom = 0.dp) // Adjust the value as needed for spacing
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
            AddPillButton(navController)
            NavBar()
        }






}

@Composable
fun NavBar(){
    Row(
        Modifier
            .height(80.dp)
            .padding(0.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
        ){
        NavItem(Icons.Rounded.DateRange,"Calender", Color(0xFF227EBA) )
        NavItem(Icons.Rounded.Email,"Message" , Color(0xFF227EBA))
        NavItem(Icons.Rounded.Face,"ChatBot", Color(0xFF227EBA) )
        NavItem(Icons.Rounded.Settings,"Setting", Color(0xFF227EBA) )
    }
}

@Composable
fun RowScope.NavItem(icon: ImageVector,description: String, tint: Color){
    Button(onClick = {
        // nav to relating pages
        },
        Modifier
            .weight(1f)
            .fillMaxHeight(),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors()
    ){
        Icon(icon, description,
            Modifier
                .size(40.dp)
                .weight(1f),
            tint = tint
        )
    }

}

@Composable
fun AddPillButton(
    navController: NavController
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                navController.navigate(NavigationPath.PILL_ADD_PAGE.route)
            },
            Modifier.padding(horizontal = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                Modifier.size((40.dp))
            )
        }
    }
}


