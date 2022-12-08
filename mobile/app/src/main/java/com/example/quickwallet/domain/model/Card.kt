package com.example.quickwallet.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


enum class CardType {
    STANDARD,
    UNKNOWN
}

@Parcelize
data class Card(
    val id: String,
    val type: CardType,
    val barcodeData: String,
    val shopName: String,
    val imageUrl: String?,
    val color: String?,
    val category: String
): Parcelable

val cards = listOf(
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    ),
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    ),
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    ),
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    ),
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    ),
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    ),
    Card(
        "",
        CardType.STANDARD,
        "",
        "",
        "Пятерочка",
        "https://www.tadviser.ru/images/e/ed/%D0%9F%D1%8F%D1%82%D1%91%D1%80%D0%BE%D1%87%D0%BA%D0%B0_LOGO.jpg",
        ""
    )
)