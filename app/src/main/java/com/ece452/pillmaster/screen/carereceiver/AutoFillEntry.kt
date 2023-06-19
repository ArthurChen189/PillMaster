package com.ece452.pillmaster.screen.carereceiver

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun AutoFillEntry(
    navController: NavController
){
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        ScanPhotoScreen(navController = navController)
    }
}