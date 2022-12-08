package com.example.quickwallet.presentation.ui.views

import BarCodeAnalyser
import android.Manifest
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.example.quickwallet.presentation.viewmodel.PhotoViewMode
import com.example.quickwallet.presentation.viewmodel.PhotoViewModel
import com.example.quickwallet.presentation.viewmodel.ShopsViewModel
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
    shopsViewModel: ShopsViewModel,
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
        CameraPreview(viewModel, navController, mode)
    }
}

@Composable
fun CameraPreview(viewModel: PhotoViewModel, navController: NavController, mode: PhotoViewMode) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var imageCapture = remember { ImageCapture.Builder().build() }
        val cameraExecutor = remember{Executors.newSingleThreadExecutor()}

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
                        viewModel.obBarcodeDataChange(it)
                    }
                }

                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(previewView.display.rotation)
                    .build()

                val useCase: UseCase = when (mode) {
                    PhotoViewMode.IMAGE_CAPTURE -> imageCapture
                    PhotoViewMode.SCANNING -> getImageAnalysis(cameraExecutor, barcodeAnalyser)
                }

                cameraProviderFuture.addListener({
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider, previewView, lifecycleOwner, useCase)
                }, ContextCompat.getMainExecutor(context))
            }
        )
        TransparentClipLayout(
            modifier = Modifier.fillMaxSize(),
            width = 316.dp,
            height = 186.dp,
            offsetY = 200.dp
        )
        if (mode == PhotoViewMode.IMAGE_CAPTURE) {
            PhotoButton(imageCapture,cameraExecutor,viewModel::onTakePhoto,)
        }
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

@Composable
fun PhotoButton(
    imageCapture: ImageCapture,
    executorService: ExecutorService,
    takePhoto: (ImageCapture, ExecutorService, () -> Unit) -> Unit,
    action: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .padding(bottom = 20.dp),
        onClick = {
            takePhoto(imageCapture, executorService) {
                action()
            }
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

@Composable
fun TransparentClipLayout(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    offsetY: Dp
) {

    val offsetInPx: Float
    val widthInPx: Float
    val heightInPx: Float

    with(LocalDensity.current) {
        offsetInPx = offsetY.toPx()
        widthInPx = width.toPx()
        heightInPx = height.toPx()
    }

    Canvas(modifier = modifier) {

        val canvasWidth = size.width

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRect(Color(0x77000000))

            // Source
            drawRoundRect(
                topLeft = Offset(
                    x = (canvasWidth - widthInPx) / 2,
                    y = offsetInPx
                ),
                size = Size(widthInPx, heightInPx),
                cornerRadius = CornerRadius(30f, 30f),
                color = Color.Transparent,
                blendMode = BlendMode.Clear
            )
            restoreToCount(checkPoint)
        }

    }
}