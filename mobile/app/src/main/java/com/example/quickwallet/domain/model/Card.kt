package com.example.quickwallet.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.UUID

enum class ShopCategory{
    FOOD,
    CLOTHES,
    SPORT,
    ANY
}

enum class CardType{
    STANDARD,
    UNKNOWN
}

@Parcelize
data class Card(
    val id: String,
    val type: CardType,
    val barcodeData: String,
    val note: String,
    val shopName: String,
    val imageUrl: String?,
    val color: String?,
    val category: ShopCategory
): Parcelable

@Parcelize
data class ViewCardData(
    val id: String,
    val type: CardType,
    val barcodeData: String,
    val note: String,
    val shopName: String,
    val imageUrl: String?,
    val color: String?,
    val category: ShopCategory,
    val barcodeImageUri: String
): Parcelable
