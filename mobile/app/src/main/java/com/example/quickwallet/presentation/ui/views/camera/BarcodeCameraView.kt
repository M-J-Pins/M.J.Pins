package com.example.quickwallet.presentation.ui.views.camera

import BarCodeAnalyser
import CameraPreview
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.quickwallet.presentation.navigation.Screen
import com.example.quickwallet.presentation.ui.views.camera.CameraCapture
import com.example.quickwallet.presentation.ui.views.camera.TransparentClipLayout
import com.example.quickwallet.presentation.ui.views.camera.cropRect
import com.example.quickwallet.presentation.ui.views.camera.toPx
import com.example.quickwallet.presentation.viewmodel.CardViewModel
import com.example.quickwallet.presentation.viewmodel.ShopsViewModel
import com.example.quickwallet.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import getImageAnalysis
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeCameraView(
    cardViewModel: CardViewModel,
    navController: NavController,
) {
    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        BarcodeCamera(
            obBarcodeDataChange = {cardViewModel.onBarcodeDataChange(it)}
        )
        LaunchedEffect(key1 = cardViewModel.barcodeData) {
            if (cardViewModel.barcodeData.isNotEmpty()) {
                navController.navigate(Screen.QuickWallet.AddCard.route)
                Log.d("QQQQQQQQQQ",cardViewModel.barcodeData)
            }
        }
        TransparentClipLayout(
            modifier = Modifier.fillMaxSize(),
            width = 316.dp,
            height = 186.dp,
            offsetY = 250.dp
        )
    } else {
        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun BarcodeCamera(
    obBarcodeDataChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { AndroidViewContext ->
                previewViewConfig(context = AndroidViewContext).apply {
                }
            },
            update = { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                val barcodeAnalyser = BarCodeAnalyser { barcode ->
                    Log.d("ANALYZING_BARCODE", "trying to analyze")
                    Log.d("ANALYZING_BARCODE", "${barcode.rawValue}")
                    barcode.rawValue?.let {
                        obBarcodeDataChange(it)
                    }
                }
                cameraProviderFuture.addListener({
                    val useCase: UseCase = getImageAnalysis(cameraExecutor, barcodeAnalyser)
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider, previewView, lifecycleOwner, useCase)
                }, ContextCompat.getMainExecutor(context))
            }
        )
    }
}

fun bindPreview(
    cameraProvider: ProcessCameraProvider,
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
    useCases: UseCase
) {
    val cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()
    val preview: Preview = Preview.Builder()
        .build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

    try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            useCases
        )
    } catch (e: Exception) {
        Log.d(Constants.cameraPreviewLogTag, "CameraPreview: ${e.localizedMessage}")
    }
}

fun previewViewConfig(context: Context): PreviewView {

    return PreviewView(context)
        .apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE

        }
}