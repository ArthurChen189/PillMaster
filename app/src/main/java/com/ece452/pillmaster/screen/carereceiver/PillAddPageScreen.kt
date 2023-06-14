package com.ece452.pillmaster.screen.common

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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillAddPageScreen() {
    var infoField1 by remember { mutableStateOf("") }
    var infoField2 by remember { mutableStateOf("") }
    var infoField3 by remember { mutableStateOf("") }
    var infoField4 by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(
            value = infoField1,
            onValueChange = { infoField1 = it },
            label = { Text("*Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = infoField2,
            onValueChange = { infoField2 = it },
            label = { Text("*Direction") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = infoField3,
            onValueChange = { infoField3 = it },
            label = { Text("*Start date") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = infoField4,
            onValueChange = { infoField4 = it },
            label = { Text("*End date") },
            modifier = Modifier.fillMaxWidth()
        )

        CareGiverDropdownMenu()

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
                if (infoField1.isNotEmpty() &&
                    infoField2.isNotEmpty() &&
                    infoField3.isNotEmpty() &&
                    infoField4.isNotEmpty()
                ) {
                    // All required fields are filled
                    // Process the input data here
                } else {
                    // Display an error message or perform an action for incomplete form
                }
                // Perform the button click action here
                // Access infoField1, infoField2, ..., infoField5, and isChecked variables to process the data
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
fun CareGiverDropdownMenu() {
    val coffeeDrinks = arrayOf("PERSON1", "PERSON2", "PERSON3", "None")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("None") }

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
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

