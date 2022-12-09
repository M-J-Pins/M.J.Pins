package com.example.quickwallet.repository.impl

import com.example.quickwallet.network.api.CardApi
import com.example.quickwallet.network.request.card.QuickCardsRequest
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest
import com.example.quickwallet.network.response.CardResponse
import com.example.quickwallet.repository.CardRepository
import retrofit2.Response

class CardRepositoryImpl(
    private val cardApi: CardApi
) : CardRepository {
    override suspend fun addUnknownCard(
        token: String,
        barcodeData: String,
        shopName: String,
        category: String
    ): Response<CardResponse> = cardApi.addUnknownCard(
        token,
        UnknownCardRequest(barcodeData, shopName, category)
    )

    override suspend fun addStandardCard(
        token: String,
        shopId: String,
        barcodeData: String,
    ): Response<CardResponse> =
        cardApi.addStandardCard(token, StandardCardRequest(shopId, barcodeData))


    override suspend fun getAllCards(token: String): Response<List<CardResponse>> =
        cardApi.getAllCards(token)

    override suspend fun getQuickCards(
        token: String,
        latitude: Double,
        longitude: Double
    ): Response<List<CardResponse>> =
        cardApi.getQuickCards(QuickCardsRequest(token, longitude, latitude))

    override suspend fun deleteCard(token: String, id: String): Response<String> =
        cardApi.deleteCard(token, id)

}