package com.example.quickwallet.network.request.card

import javax.inject.Named

data class UnknownCardRequest(
    @Named("barcode_data")
    val barcodeData: String,
    @Named("note")
    val note: String,
    @Named("shop_name")
    val shopName: String,
    @Named("category")
    val category: String
)
