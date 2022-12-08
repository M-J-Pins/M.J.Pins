package com.example.quickwallet.network.services.card

import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.network.request.card.QuickCardsRequest
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest

interface CardService {
    suspend fun addStandardCard(token: String,standardCardDto: StandardCardRequest): Card?
    suspend fun addUnknownCard(token: String,unknownCardRequest: UnknownCardRequest): Card?
    suspend fun getAllCards(token: String): List<Card>?
    suspend fun getQuickCards(quickCardsRequest: QuickCardsRequest): MutableList<Card>?
}