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
        barcodeData: String,
        note: String,
        shopName: String,
        category: String
    ): Card? = cardService.addUnknownCard(UnknownCardRequest(barcodeData, note, shopName, category))

    override suspend fun addStandardCard(shopId: String, barcodeData: String, note: String): Card? =
        cardService.addStandardCard(StandardCardRequest(shopId, barcodeData, note))
}