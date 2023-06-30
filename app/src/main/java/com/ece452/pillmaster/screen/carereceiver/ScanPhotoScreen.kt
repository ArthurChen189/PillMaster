package com.ece452.pillmaster.screen.carereceiver

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ece452.pillmaster.R
import com.ece452.pillmaster.utils.ScanUtils
import com.ece452.pillmaster.viewmodel.LoginViewModel
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageUtils
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


@Composable
fun ScanPhotoScreen(
    navController: NavController,
    scanUtils: ScanUtils = hiltViewModel()
) {
    lateinit var image: MutableState<Bitmap>
    lateinit var text: MutableState<String>
    var capturedImageUri by remember { mutableStateOf("") }
    val context = LocalContext.current
    val permissionCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            scanUtils.takePic(context)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

    }


    image = remember {
        mutableStateOf(Bitmap.createBitmap(context.getDrawable(R.drawable.camera)!!.toBitmap()))
    }

    text = remember {
        mutableStateOf("")
    }

    var mGetContent = rememberLauncherForActivityResult(
        GetContent()
    ) { uri ->
        if (uri != null && !uri.equals(Uri.EMPTY)){
            capturedImageUri = uri.toString()
            //  after choosing the desired image, we want to get a bitmap from the uri
            image.value = ImageUtils.getInstance().zza(context.contentResolver, uri)
        }


    }

    val permissionImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            mGetContent.launch("image/*")
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment. CenterHorizontally
    ) {
        Image(
            bitmap = image.value.asImageBitmap(),
            contentDescription = "Captured Image",

        )
        Spacer(modifier = Modifier.height(20.dp))

        SelectionContainer() { // selection container to make the user able to select text and copy it
            Text(text = text.value)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { // button to take a pic with the camera
            val permissionCheckResult = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            )

            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                scanUtils.takePic(context)
            }else {
                permissionCameraLauncher.launch(android.Manifest.permission.CAMERA)
            }

        },
            modifier = Modifier
                .fillMaxWidth()
                .size(55.dp),
            shape = CutCornerShape(0.dp),
        ) {
            androidx.compose.material3.Text(text = "Take a photo")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { // button to choose an image

            val permissionCheckResult = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES
            )

            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                mGetContent.launch("image/*")
            }else {
                permissionImageLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .size(55.dp),
            shape = CutCornerShape(0.dp),
           // colors = ButtonDefaults.outlinedButtonColors()
        ) {
            androidx.compose.material3.Text(text = "Upload from gallery")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { // button to extract the text from the image
                scanUtils.autoFill(context,capturedImageUri,text,navController)
                      },
        ) {
            androidx.compose.material3.Text(text = "Auto fill description")
        }
    }
}


