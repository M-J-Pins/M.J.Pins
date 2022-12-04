package com.example.quickwallet.presentation.viewmodel

import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.domain.model.ViewCardData
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CardViewModel
@Inject constructor(
    private val cardRepository: CardRepository,
    private val state: SavedStateHandle,
    private val app: BaseApplication
) : ViewModel() {
    var cardData: List<ViewCardData> by state.saveable {
        mutableStateOf(emptyList())
    }

    fun addCard() {
        val file = File(app.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "barcodes")


    }

}