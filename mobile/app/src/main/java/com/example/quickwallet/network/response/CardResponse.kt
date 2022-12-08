package com.example.quickwallet.network.response

import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.domain.model.CardType
import com.google.gson.annotations.SerializedName
import java.util.*

data class CardResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: CardType,
    @SerializedName("barcode_data")
    val barcodeData: String,
    @SerializedName("shop_name")
    val shopName: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("category")
    val category: String
)

fun CardResponse.toCard(): Card = Card(
     id = this.id,
    type = this.type,
    barcodeData = this.barcodeData,
    shopName = this.shopName,
    imageUrl = this.imageUrl,
    color = this.color,
    category = this.category
)
