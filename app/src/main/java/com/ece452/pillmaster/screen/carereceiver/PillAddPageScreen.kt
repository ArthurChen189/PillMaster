package com.ece452.pillmaster.screen.common

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.PillAddPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillAddPageScreen(
    navController: NavController
) {
    var pillName by remember { mutableStateOf("") }
    var direction by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("None") }
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(
            value = pillName,
            onValueChange = { pillName = it },
            label = { Text("*Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = direction,
            onValueChange = { direction = it },
            label = { Text("*Direction") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("*Start date") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("*End date") },
            modifier = Modifier.fillMaxWidth()
        )

        CareGiverDropdownMenu(
            selectedText = selectedOption,
            onSelectedTextChange = { selectedOption = it },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
//                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Send the pill information to your CareGiver")
        }

        Button(
            onClick = {
                if (pillName.isNotEmpty() &&
                    direction.isNotEmpty() &&
                    startDate.isNotEmpty() &&
                    endDate.isNotEmpty()
                ) {
                    // All required fields are filled
                    // submit the input data here
                    PillAddPageViewModel().newPillSubmit(pillName,direction, startDate, endDate, selectedOption, isChecked)
                    navController.navigate(NavigationPath.CARE_RECEIVER_HOMEPAGE.route)
                } else {
                    Toast.makeText(context, "fill every *input", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGiverDropdownMenu(
    selectedText: String,
    onSelectedTextChange: (String) -> Unit,
) {
    val careGivers = arrayOf("PERSON1", "PERSON2", "PERSON3", "None")  //getCareGivers()
    var expanded by remember { mutableStateOf(false) }

    Box() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = if(selectedText == "None"){
                    "Plz choose a Caregiver"
                }else{
                    selectedText
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                careGivers.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onSelectedTextChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

