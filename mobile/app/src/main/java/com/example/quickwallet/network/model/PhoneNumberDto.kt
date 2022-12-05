package com.example.quickwallet.network.model

import com.google.gson.annotations.SerializedName

data class PhoneNumberDto(
    @SerializedName("phone")
    val phone: String
)