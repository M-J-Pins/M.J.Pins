package com.example.quickwallet.network.services.card

import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest

interface CardService {
    suspend fun addStandardCard(standardCardDto: StandardCardRequest): Card?
    suspend fun addUnknownCard(unknownCardRequest: UnknownCardRequest): Card?
}