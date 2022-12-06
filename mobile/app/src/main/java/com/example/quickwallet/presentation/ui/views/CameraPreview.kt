package com.example.quickwallet.presentation.ui.views

import BarCodeAnalyser
import android.Manifest
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.presentation.viewmodel.PhotoViewMode
import com.example.quickwallet.presentation.viewmodel.PhotoViewModel
import com.example.quickwallet.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import getImageAnalysis
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraView(
    viewModel: PhotoViewModel,
    navController: NavController,
    mode: PhotoViewMode
) {
    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)
    if (!cameraPermissionState.status.isGranted) {
        SideEffect {
            cameraPermissionState.launchPermissionRequest()
        }
    }
    AppTheme {
        CameraPreview(navController, mode)
    }
}

@Composable
fun CameraPreview(navController: NavController, mode: PhotoViewMode) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { AndroidViewContext ->
                previewViewConfig(context = AndroidViewContext)
            },
            modifier = Modifier
                .fillMaxSize(),
            update = { previewView ->

                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                val barcodeAnalyser = BarCodeAnalyser { barcode ->
                    Toast.makeText(context, barcode.rawValue, Toast.LENGTH_LONG)
                }

                val useCase: UseCase = when (mode) {
                    PhotoViewMode.IMAGE_CAPTURE -> ImageCapture.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()
                    PhotoViewMode.SCANNING -> getImageAnalysis(cameraExecutor, barcodeAnalyser)
                }

                cameraProviderFuture.addListener({
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider, previewView, lifecycleOwner, useCase)
                }, ContextCompat.getMainExecutor(context))
            }
        )

        CardRectHelper()
        PhotoButton()
    }

}


fun previewViewConfig(context: Context) = PreviewView(context)
    .apply {
        this.scaleType = PreviewView.ScaleType.FILL_CENTER
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
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
    var preview: Preview = Preview.Builder()
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

@Composable
fun BoxScope.CardRectHelper() {
    Surface(
        modifier = Modifier
            .align(Alignment.Center)
            .height(189.dp)
            .width(316.dp),
        shape = RoundedCornerShape(corner = CornerSize(20.dp)),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary),
        color = Color.Transparent
    ) {}

}

@Composable
fun PhotoButton() {
    IconButton(
        modifier = Modifier
            .padding(bottom = 20.dp),
        onClick = {
            Log.d(Constants.cameraPreviewLogTag, "take photo btn was clicked")
        },
        content = {
            Icon(
                imageVector = Icons.Sharp.Lens,
                contentDescription = "Take picture",
                tint = Color.White,
                modifier = Modifier
                    .size(80.dp)
                    .padding(1.dp)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    )
}
