package com.example.quickwallet.network.response

import com.example.quickwallet.domain.model.Shop
import com.google.gson.annotations.SerializedName

data class ShopResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("add_card_action_id")
    val addCardActionId: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon_url")
    val iconUrl: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("card_image_url")
    val cardImageUrl: String
)

fun ShopResponse.toShop(): Shop {
    return Shop(id, addCardActionId, name, iconUrl, category, cardImageUrl)
}