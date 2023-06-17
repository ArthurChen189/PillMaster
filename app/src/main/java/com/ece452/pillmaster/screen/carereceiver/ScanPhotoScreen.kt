package com.ece452.pillmaster.screen.carereceiver

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.ece452.pillmaster.R
import com.google.mlkit.vision.common.internal.ImageUtils
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions




@Composable
fun ScanPhotoScreen(context: Context) {
//
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val extractedText = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        TextRecognitionView(
//            context = context,
//            lifecycleOwner = lifecycleOwner,
//            extractedText = extractedText
//        )
//        Text(
//            text = extractedText.value,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White)
//                .padding(16.dp)
//        )
//    }
    lateinit var image: MutableState<Bitmap>
    lateinit var text: MutableState<String>
    var capturedImageUri by remember { mutableStateOf("") }
    image = remember {
        mutableStateOf(Bitmap.createBitmap(context.getDrawable(R.drawable.camera)!!.toBitmap()))
    }

    text = remember {
        mutableStateOf("")
    }

    var mGetContent = rememberLauncherForActivityResult(
        GetContent()
    ) { uri ->
        capturedImageUri = uri.toString()
        //  after choosing the desired image, we want to get a bitmap from the uri
        image.value = ImageUtils.getInstance().zza(context.contentResolver, uri!!)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
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

            takePic(context)
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
            mGetContent.launch("image/*")
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
        Button(onClick = { // button to extract the text from the image
            detectText(context,capturedImageUri,text)
        },
            modifier = Modifier
                .fillMaxWidth()
                .size(55.dp),
            shape = CutCornerShape(0.dp),
        ) {
            androidx.compose.material3.Text(text = "Auto fill description")
        }
    }
}

private fun takePic(context: Context) {
    //  Navigating to the camera app on the phone to take a photo
    val takePictureIntent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
    context.startActivity(takePictureIntent)
}


private fun detectText(context: Context,capturedImageUri: String,text: MutableState<String>) {
    // here we are getting an input image from the uri of the chosen image to process it with the text recognizer instance
    val inputImage = InputImage.fromFilePath(context, capturedImageUri.toUri())
    val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val result: Task<Text> = textRecognizer.process(inputImage).addOnSuccessListener {
        text.value =
            it.text // after processing, we will set the extracted text from the image to the text variable
    }.addOnFailureListener {
        Toast.makeText(context, "Extraction of text failed", Toast.LENGTH_SHORT).show()
    }
}


//@Deprecated("Deprecated in Java")
//fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray ) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    if (requestCode == 12 && grantResults.isNotEmpty()) {
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            Toast.makeText( this, "Granted", Toast.LENGTH_SHORT ).show()
//    }
//}
//
//
//private fun requestPermissions(context: Context) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.CAMERA),
//            12
//        )
//    }
//}

//@Composable
//fun TextRecognitionView(
//    context: Context,
//    lifecycleOwner: LifecycleOwner,
//    extractedText: MutableState<String>
//) {
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//    var preview by remember { mutableStateOf<Preview?>(null) }
//    val executor = ContextCompat.getMainExecutor(context)
//    val cameraProvider = cameraProviderFuture.get()
//    val textRecognizer = remember { TextRecognition.getClient() }
//    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
//
//    Box {
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.7f),
//            factory = { ctx ->
//                val previewView = PreviewView(ctx)
//                cameraProviderFuture.addListener({
//                    val imageAnalysis = ImageAnalysis.Builder()
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build()
//                        .apply {
//                            setAnalyzer(cameraExecutor, ObjectDetectorImageAnalyzer(textRecognizer, extractedText))
//                        }
//                    val cameraSelector = CameraSelector.Builder()
//                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                        .build()
//                    cameraProvider.unbindAll()
//                    cameraProvider.bindToLifecycle(
//                        lifecycleOwner,
//                        cameraSelector,
//                        imageAnalysis,
//                        preview
//                    )
//                }, executor)
//                preview = Preview.Builder().build().also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//                previewView
//            }
//        )
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(15.dp)
//                .align(Alignment.TopStart)
//        ) {
//            IconButton(
//                onClick = { Toast.makeText(context, "Back Clicked", Toast.LENGTH_SHORT).show() }
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBack,
//                    contentDescription = "back",
//                    tint = Color.White
//                )
//            }
//        }
//    }
//}
//
//class ObjectDetectorImageAnalyzer(
//    private val textRecognizer: TextRecognizer,
//    private val extractedText: MutableState<String>
//): ImageAnalysis.Analyzer {
//    @SuppressLint("UnsafeOptInUsageError")
//    override fun analyze(imageProxy: ImageProxy) {
//
//        val mediaImage = imageProxy.image
//        if (mediaImage != null) {
//            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//
//            textRecognizer.process(image)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        extractedText.value = it.result?.text ?: ""
//                    }
//                    imageProxy.close()
//                }
//        }
//    }
//}
