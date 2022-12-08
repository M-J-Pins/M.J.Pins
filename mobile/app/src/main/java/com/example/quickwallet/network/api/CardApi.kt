package com.example.quickwallet.network.api

import com.example.quickwallet.network.request.card.QuickCardsRequest
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest
import com.example.quickwallet.network.response.CardResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Named

interface CardApi {
    @POST("cards/add_standard")
    fun addStandardCard(@Named("token") token: String, standardCardDto: StandardCardRequest): Call<CardResponse>
    @POST("cards/add_unknown")
    fun addUnknownCard(@Named("token") token: String, unknownCardRequest: UnknownCardRequest): Call<CardResponse>
    @GET("cards/my")
    fun getAllCards(@Named("token") token: String): Call<List<CardResponse>>
    @GET("cards/quick")
    fun getQuickCards(quickCardsRequest: QuickCardsRequest): Call<MutableList<CardResponse>>
}