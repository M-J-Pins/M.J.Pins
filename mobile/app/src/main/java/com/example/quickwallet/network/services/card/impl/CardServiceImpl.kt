package com.example.quickwallet.network.services.card.impl

import android.util.Log
import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.network.api.CardApi
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest
import com.example.quickwallet.network.response.CardResponse
import com.example.quickwallet.network.response.toCard
import com.example.quickwallet.network.services.card.CardService
import com.example.quickwallet.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardServiceImpl(
    private val retrofit: CardApi
): CardService {
    override suspend fun addStandardCard(token: String,standardCardDto: StandardCardRequest): Card? {
        var card: Card? = null
        retrofit.addStandardCard(token, standardCardDto).enqueue(
            object : Callback<CardResponse> {
                override fun onResponse(
                    call: Call<CardResponse>,
                    response: Response<CardResponse>
                ) {
                    card = response.body()?.toCard()
                    Log.d(Constants.cardServiceLogTag, "standard card successfully sent and received")
                }

                override fun onFailure(call: Call<CardResponse>, t: Throwable) {
                    card = null
                    Log.d(Constants.cardServiceLogTag, "standard card did not send and receive")
                }

            })
        return card
    }

    override suspend fun addUnknownCard(token: String,unknownCardRequest: UnknownCardRequest): Card? {
        var card: Card? = null
        retrofit.addUnknownCard(token, unknownCardRequest).enqueue(
            object : Callback<CardResponse> {
                override fun onResponse(
                    call: Call<CardResponse>,
                    response: Response<CardResponse>
                ) {
                    card = response.body()?.toCard()
                    Log.d(Constants.cardServiceLogTag, "unknown card successfully sent and received")
                }
                override fun onFailure(call: Call<CardResponse>, t: Throwable) {
                    card = null
                    Log.d(Constants.cardServiceLogTag, "unknown card did not send and receive")
                }

            })
        return card
    }

    override suspend fun getAllCards(token: String): List<Card>? {
        var cardList: List<Card>? = null
        retrofit.getAllCards(token).enqueue(
            object : Callback<List<CardResponse>> {
                override fun onResponse(
                    call: Call<List<CardResponse>>,
                    response: Response<List<CardResponse>>
                ) {
                    cardList = response.body()?.map { it.toCard() }
                    Log.d(Constants.cardServiceLogTag, "getAllCards() successful")
                }
                override fun onFailure(call: Call<List<CardResponse>>, t: Throwable) {
                    cardList = null
                    Log.d(Constants.cardServiceLogTag, "getAllCards() error")
                }
            }
        )
        return cardList
    }
}