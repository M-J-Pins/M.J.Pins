package com.example.quickwallet.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
    val id: String,
    val addCardActionId: String?,
    val name: String,
    val iconUrl: String,
    val category: String,
    val cardImageUrl: String
):Parcelable
