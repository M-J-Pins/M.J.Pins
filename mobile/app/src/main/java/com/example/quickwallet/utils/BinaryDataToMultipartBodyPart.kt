package com.example.quickwallet.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

fun ByteArray.binaryDataToMultipartBodyPart(name: String) : MultipartBody.Part{
    val file = RequestBody.create(MediaType.parse("multipart/form-data"),this)
    return MultipartBody.Part.createFormData(name,null,file)
}