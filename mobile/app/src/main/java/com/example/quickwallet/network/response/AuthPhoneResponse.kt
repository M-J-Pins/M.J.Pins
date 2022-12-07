package com.example.quickwallet.network.response

import com.google.gson.annotations.SerializedName

data class AuthPhoneResponse(
    @SerializedName("token")
    val token: String?
)