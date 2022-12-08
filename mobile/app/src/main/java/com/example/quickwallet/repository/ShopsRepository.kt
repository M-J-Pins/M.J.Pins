package com.example.quickwallet.repository

import com.example.quickwallet.domain.model.Shop

interface ShopsRepository {
    suspend fun getShops(): List<String>?
    suspend fun getMostSimilarity(token: String, data: ByteArray): Shop?
}