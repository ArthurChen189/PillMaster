package com.ece452.pillmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ece452.pillmaster.ui.theme.PillMasterTheme
import dagger.hilt.android.AndroidEntryPoint
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO - Use https://developer.android.com/reference/android/window/SplashScreen to create the splash.
    // TODO - Create App Icon resource and register it in AndroidManifest.xml
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
        println("Python version: " + Python.version())
        val py = Python.getInstance()
        val module = py.getModule("my-python")
        
        val values = module.callAttr("myFunc", 1, 2).toString()
        println("Python values: " + values)

        setContent {
            PillMasterTheme {
                PillMasterApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillMasterApp() {
    // Inflate the reminder screen as an example for now.
    // TODO - This reminder screen is an example.
    // ReminderScreen()

    // TODO !!! This is the entry composable.
    val navController = rememberNavController()
    Scaffold() { innerPadding ->
        PillMasterNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PillMasterTheme {
        Greeting("Android")
    }
}
