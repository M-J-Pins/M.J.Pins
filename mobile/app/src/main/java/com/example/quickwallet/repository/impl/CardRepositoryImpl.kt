package com.example.quickwallet.repository.impl

import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest
import com.example.quickwallet.network.services.card.CardService
import com.example.quickwallet.repository.CardRepository
import java.util.*

class CardRepositoryImpl(
    private val cardService: CardService
) : CardRepository {
    override suspend fun addUnknownCard(
        token: String,
        barcodeData: String,
        note: String,
        shopName: String,
        category: String
    ): Card? = cardService.addUnknownCard(token, UnknownCardRequest(barcodeData, note, shopName, category))

    override suspend fun addStandardCard(token: String, shopId: String, barcodeData: String, note: String): Card? =
        cardService.addStandardCard(token, StandardCardRequest(shopId, barcodeData, note))

    override suspend fun getAllCards(token: String): List<Card>? = cardService.getAllCards(token)
}