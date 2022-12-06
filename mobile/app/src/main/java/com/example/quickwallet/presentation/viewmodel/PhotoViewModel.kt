package com.example.quickwallet.presentation.viewmodel

import android.graphics.Rect
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.presentation.BaseApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel
@Inject
constructor(
private val app: BaseApplication
) : ViewModel() {

    fun onTakePhoto(imageCapture: ImageCapture, executor: ExecutorService) {
        viewModelScope.launch(Dispatchers.IO) {
            imageCapture.takePicture(
                executor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val img = image.image

                        image.close()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            app.applicationContext,
                            "Что-то пошло не так",
                            Toast.LENGTH_LONG
                        )
                    }
                }
            )

        }
    }
}

enum class PhotoViewMode {
    SCANNING,
    IMAGE_CAPTURE
}