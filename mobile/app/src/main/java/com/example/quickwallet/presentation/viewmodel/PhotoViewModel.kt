package com.example.quickwallet.presentation.viewmodel

import ImageProxyUtils
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.utils.Constants
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

    val barcodeData = mutableStateOf("")



    fun obBarcodeDataChange(data: String) {
        barcodeData.value = data
        Log.d(Constants.photoViewModel, ::obBarcodeDataChange.name)
    }

    fun onTakePhoto(imageCapture: ImageCapture, executor: ExecutorService, token: String, action:(String,ByteArray)->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(Constants.photoViewModel, ::onTakePhoto.name)
            imageCapture.takePicture(
                executor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val data = ImageProxyUtils.getByteArray(image)
                        data?.let {
                            Log.d(Constants.photoViewModel, "sending data")
                            action(token,data)
                        }
                        image.close()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.d(Constants.photoViewModel,"ERROR TAKE PHOTO")
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