package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

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
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics { contentDescription = "Care receiver's home screen" }
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // TODO - Build this screen as per the Figma file.
        LazyColumn(modifier = Modifier.fillMaxHeight().padding(16.dp)) {
            items(medicines) { medicine ->
                Text(text = medicine, modifier = Modifier.padding(8.dp))
            }
        }
    }
}