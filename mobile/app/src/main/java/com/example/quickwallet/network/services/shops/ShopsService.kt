package com.example.quickwallet.network.services.shops

import com.example.quickwallet.domain.model.Shop

interface ShopsService {
    suspend fun getShops(): List<String>?
    suspend fun getMostSimilarityShop(token: String, data: ByteArray): Shop?
}