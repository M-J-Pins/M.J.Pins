package com.example.quickwallet.presentation.ui.views

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickwallet.presentation.ui.views.camera.CameraCapture
import com.example.quickwallet.presentation.ui.views.camera.TransparentClipLayout
import com.example.quickwallet.presentation.ui.views.camera.cropRect
import com.example.quickwallet.presentation.ui.views.camera.toPx
import com.example.quickwallet.presentation.viewmodel.ShopsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraCaptureView(
    shopsViewModel: ShopsViewModel,
    navController: NavController,
    token: String
) {
    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraCapture(){
            shopsViewModel.getPhotoFilepath(it)
        }
        val context = LocalContext.current

        LaunchedEffect(key1 = shopsViewModel.filepath){
            if (shopsViewModel.filepath.isNotBlank()) {
                val bitmap = BitmapFactory.decodeFile(shopsViewModel.filepath)
                val res = bitmap?.cropRect(200.toPx.toInt(), 316.toPx.toInt(),186.toPx.toInt())
                res?.let {
                    val bos = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG,80,bos)
                    val tmp = File(context.cacheDir,"card_photo")
                    val fos = FileOutputStream(tmp)
                    fos.write(bos.toByteArray())
                    shopsViewModel.getMostSimilarities(token,tmp)
                    Log.d("ImageCapture", "!!!!!!!!!!!!!!!!!!!")
                }
            }
        }
        TransparentClipLayout(modifier = Modifier.fillMaxSize(), width = 316.dp, height = 186.dp, offsetY = 200.dp)
    } else {
        LaunchedEffect(Unit){
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

