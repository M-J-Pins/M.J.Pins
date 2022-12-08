package com.example.quickwallet.network.services.shops

import com.example.quickwallet.domain.model.Shop
import com.example.quickwallet.network.api.ShopsApi
import com.example.quickwallet.network.response.toShop
import com.example.quickwallet.utils.binaryDataToMultipartBodyPart

import okhttp3.MediaType
import okhttp3.RequestBody

class ShopsServiceImpl(
    private val retrofit: ShopsApi
) : ShopsService {
    override suspend fun getShops(): List<String>? {
        return retrofit.getShopCategories().execute().body()?.categories
    }

    override suspend fun getMostSimilarityShop(token: String, data: ByteArray): Shop? {
        return retrofit.getMostSimilarities(token, data.binaryDataToMultipartBodyPart("card_photo"))
            .execute()
            .body()?.toShop()
    }
}
