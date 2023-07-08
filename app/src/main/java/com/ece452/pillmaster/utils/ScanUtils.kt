package com.ece452.pillmaster.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.ParseException
import javax.inject.Inject
import java.util.regex.Pattern
import java.text.SimpleDateFormat

@HiltViewModel
class ScanUtils @Inject constructor(): ViewModel() {

    //  Opening the camera app on the phone in order to capture a photo
     fun takePic(context: Context) {
        val takePictureIntent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
        context.startActivity(takePictureIntent)
    }

    // Obtaining an input image from the URI of the selected image to process it using the text recognizer instance
     fun autoFill(context: Context, capturedImageUri: String, text: MutableState<String>, navController: NavController) {
        val inputImage = InputImage.fromFilePath(context, capturedImageUri.toUri())
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result: Task<Text> = textRecognizer.process(inputImage).addOnSuccessListener {
            text.value = it.text
            //extract detailed info for this reminder
            extractListOfInfo(it,navController )
        }.addOnFailureListener {
            Toast.makeText(context, "Extraction of text failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun extractListOfInfo( inputText: Text, navController: NavController ){

        val pattern = Pattern.compile("\\d{1,2}:\\d{2}\\s*[ap]m", Pattern.CASE_INSENSITIVE)

        var discription = ""
        var pillName = ""
        var reminderTime = ""
        var startDate = ""
        for (block in inputText.textBlocks) {
            val blockText = block.text
            if(blockText.startsWith("Date", ignoreCase = true)){
                startDate = blockText.substring(5)
            }
            if(blockText.contains("Disp:") || blockText.contains("Sig:")){
                pillName = block.lines[0].text
                discription = block.lines[1].text + "\n" + block.lines[2].text
                val matcher = pattern.matcher(block.lines[2].text)
                if(matcher.find()){
                    reminderTime = matcher.group()
                }
            }

        }
        val inputFormat = SimpleDateFormat("M/d/yy")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")

        val inputTimeFormat = SimpleDateFormat("h:mm a")
        val outputTimeFormat = SimpleDateFormat("HH:mm")

        // Transfer the extracted text back to the previous screen using savedStateHandle
        if(discription.isNotEmpty()){
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("description", discription)
        }

        if(pillName.isNotEmpty()){
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("pillName", pillName)
        }

        if(reminderTime.isNotEmpty()){
            try {
                val parsedTime = inputTimeFormat.parse(reminderTime)
                val formattedTime = outputTimeFormat.format(parsedTime)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("reminderTime", formattedTime)
            } catch (e: ParseException) {
                println("Error occurred during time parsing: ${e.message}")
            }
        }

        if(startDate.isNotEmpty()){
            try {
                val parsedDate = inputFormat.parse(startDate)
                val formattedDate = outputFormat.format(parsedDate)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("startDate", formattedDate)
            } catch (e: ParseException) {
                println("Error occurred during time parsing: ${e.message}")
            }
        }

        // Pop back to the previous screen (PillAddPageScreen)
        navController.popBackStack()
    }
}