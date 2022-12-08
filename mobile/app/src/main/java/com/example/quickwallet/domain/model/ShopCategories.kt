package com.example.quickwallet.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopCategories(
    val categories: List<String>
): Parcelable
