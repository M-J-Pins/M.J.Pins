package com.example.quickwallet.network.response

import com.example.quickwallet.domain.model.ShopCategories
import com.google.gson.annotations.SerializedName


data class ShopCategoriesResponse(
    @SerializedName("categories")
    val categories: List<String>
)

fun ShopCategoriesResponse.toShopCategories() = ShopCategories(categories)