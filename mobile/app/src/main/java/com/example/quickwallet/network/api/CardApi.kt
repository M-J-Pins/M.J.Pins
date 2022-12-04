package com.example.quickwallet.network.api

import com.example.quickwallet.network.request.card.StandardCardRequest
import com.example.quickwallet.network.request.card.UnknownCardRequest
import com.example.quickwallet.network.response.CardResponse
import retrofit2.Call
import retrofit2.http.POST
import javax.inject.Named

interface CardApi {
    @POST("card/add_standard")
    fun addStandardCard(@Named("token") token: String, standardCardDto: StandardCardRequest): Call<CardResponse>
    @POST("card/add_unknown")
    fun addUnknownCard(@Named("token") token: String, unknownCardRequest: UnknownCardRequest): Call<CardResponse>
}