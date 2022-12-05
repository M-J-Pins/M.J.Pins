package com.example.quickwallet.presentation.viewmodel

import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.domain.model.ViewCardData
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CardViewModel
@Inject constructor(
    private val cardRepository: CardRepository,
    private val state: SavedStateHandle,
    private val app: BaseApplication
) : ViewModel() {
    var cards: MutableState<MutableList<Card>> = mutableStateOf(mutableListOf())

    fun fetchAllCards(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetch = cardRepository.getAllCards(token)
            if (fetch != null) {
                cards.value.addAll(fetch)
            }
        }
    }

}