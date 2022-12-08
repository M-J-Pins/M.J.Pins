package com.example.quickwallet.network.request.card

import javax.inject.Named

data class QuickCardsRequest(
    @Named("token")
    val token: String,
    @Named("longitude")
    val longitude: Double,
    @Named("latitude")
    val latitude: Double
)
