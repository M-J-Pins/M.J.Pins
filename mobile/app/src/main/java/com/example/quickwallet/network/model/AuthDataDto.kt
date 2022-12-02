package com.example.quickwallet.network.model

import com.google.gson.annotations.SerializedName

data class AuthDataDto(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("code")
    val code: String
)