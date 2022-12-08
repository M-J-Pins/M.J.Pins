package com.example.quickwallet.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.String

@Parcelize
data class AuthData(
    val phoneNumber: String,
    val code: String,
): Parcelable



