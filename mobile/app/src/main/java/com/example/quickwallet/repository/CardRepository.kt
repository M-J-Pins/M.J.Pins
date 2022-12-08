package com.example.quickwallet.repository

import com.example.quickwallet.domain.model.Card
import java.util.*

interface CardRepository {
    suspend fun addUnknownCard(token: String,barcodeData: String,note: String,shopName: String,category: String): Card?
    suspend fun addStandardCard(token: String,shopId: String, barcodeData: String,note: String): Card?
    suspend fun getAllCards(token: String): List<Card>?
    suspend fun getQuickCards(token: String, latitude: Double, longitude: Double): MutableList<Card>?
}