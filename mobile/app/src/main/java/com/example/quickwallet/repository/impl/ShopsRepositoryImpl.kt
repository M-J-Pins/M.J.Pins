package com.example.quickwallet.repository.impl

import com.example.quickwallet.domain.model.Shop
import com.example.quickwallet.network.services.shops.ShopsService
import com.example.quickwallet.repository.ShopsRepository

class ShopsRepositoryImpl(
    private val shopsService: ShopsService
): ShopsRepository {
    override suspend fun getShops(): List<String>? {
        return shopsService.getShops()
    }

    override suspend fun getMostSimilarity(token: String, data: ByteArray): Shop? {
        return shopsService.getMostSimilarityShop(token,data)
    }
}