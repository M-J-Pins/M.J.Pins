package com.example.quickwallet.network.api

import com.example.quickwallet.network.request.card.QuickCardsRequest
import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest
import com.example.quickwallet.network.response.CardResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Named

interface CardApi {
    @POST("cards/standard")
    suspend fun addStandardCard(@Named("token") token: String, standardCardDto: StandardCardRequest): Response<CardResponse>

    @POST("cards/unknown")
    suspend fun addUnknownCard(@Named("token") token: String, unknownCardRequest: UnknownCardRequest): Response<CardResponse>

    @GET("cards/my")
    suspend fun getAllCards(@Named("token") token: String): Response<List<CardResponse>>

    @GET("cards/quick")
    suspend fun getQuickCards(quickCardsRequest: QuickCardsRequest): Response<List<CardResponse>>

    @DELETE("cards/{card_id}")
    suspend fun deleteCard(@Named("token") token: String,@Path("card_id") cardId:String): Response<String>
}