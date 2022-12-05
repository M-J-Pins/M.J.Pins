package com.example.quickwallet.network.request.card

import javax.inject.Named

data class StandardCardRequest(
    @Named("shop_id")
    val shopId: String,
    @Named("barcode_data")
    val barcodeData: String,
    @Named("note")
    val note: String
)
