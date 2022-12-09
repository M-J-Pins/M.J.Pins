package com.example.quickwallet.repository

import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.network.response.CardResponse
import retrofit2.Response

interface CardRepository {
    suspend fun addUnknownCard(token: String,barcodeData: String,shopName: String,category: String): Response<CardResponse>
    suspend fun addStandardCard(token: String,shopId: String, barcodeData: String):  Response<CardResponse>
    suspend fun getAllCards(token: String): Response<List<CardResponse>>
    suspend fun getQuickCards(token: String, latitude: Double, longitude: Double): Response<List<CardResponse>>
    suspend fun deleteCard(token: String, id: String): Response<String>
}