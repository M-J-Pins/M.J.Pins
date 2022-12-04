package com.example.quickwallet.repository

import com.example.quickwallet.domain.model.Card
import java.util.*

interface CardRepository {
    suspend fun addUnknownCard(barcodeData: String,note: String,shopName: String,category: String): Card?
    suspend fun addStandardCard(shopId: String, barcodeData: String,note: String): Card?
}