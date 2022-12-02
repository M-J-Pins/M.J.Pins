package com.example.quickwallet

import BarCodeAnalyser
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber
import com.example.quickwallet.network.auth.impl.AuthServiceImpl
import com.example.quickwallet.network.model.AuthDataMapper
import com.example.quickwallet.network.model.PhoneNumberMapper
import com.example.quickwallet.repository.impl.AuthRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.example.quickwallet.ui.theme.QuickWalletTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickWalletTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var phone by remember {
                            mutableStateOf("")
                        }
                        var code by remember {
                            mutableStateOf("")
                        }
                        var isPhoneSended by remember {
                            mutableStateOf(false)
                        }

                        val authRepository = AuthRepositoryImpl(AuthServiceImpl(),AuthDataMapper(),PhoneNumberMapper())
                        val scope = rememberCoroutineScope()
                        val context = LocalContext.current
                        val networkPermission = rememberPermissionState(permission = Manifest.permission.INTERNET)

                        TextField(value = phone, onValueChange = {phone = it}, enabled = !isPhoneSended)
                        Spacer(modifier = Modifier.height(10.dp))
                        if (!isPhoneSended) {
                            Button(onClick = {
                                isPhoneSended = !isPhoneSended
                                networkPermission.launchPermissionRequest()
                                scope.launch {
                                    authRepository.phoneAuthRequest(PhoneNumber(phone))
                                }
                            }) {
                                Text(text = "send phone")
                            }
                        } else {
                            TextField(value = code, onValueChange = {code = it})
                            Button(onClick = {
                                isPhoneSended = !isPhoneSended
                                scope.launch {
                                    val token = authRepository.phoneAuth(AuthData(phone,code))
                                    token?.let {
                                        Toast.makeText(context , token, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }) {
                                Text(text = "send phone")
                            }
                        }

                        
                        
                        
                    }
                }
            }
        }
    }
}
//Spacer(modifier = Modifier.height(10.dp))
//
//val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
//
//Button(
//onClick = {
//    cameraPermissionState.launchPermissionRequest()
//}
//) {
//    Text(text = "Camera Permission")
//}
//
//Spacer(modifier = Modifier.height(10.dp))
//
//CameraPreview()

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }

    AndroidView(
        factory = { AndroidViewContext ->
            PreviewView(AndroidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        update = { previewView ->
            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let { barcodeValue ->
                            barCodeVal.value = barcodeValue
                            Toast.makeText(context, barcodeValue, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}
