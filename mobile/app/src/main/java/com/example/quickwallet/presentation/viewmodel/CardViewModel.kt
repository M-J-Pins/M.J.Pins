package com.example.quickwallet.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.domain.model.Card
import com.example.quickwallet.network.response.CardResponse
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.CardRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class CardViewModel
@Inject constructor(
    private val cardRepository: CardRepository,
    private val app: BaseApplication
) : ViewModel() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    var myCards by mutableStateOf(emptyList<CardResponse>().toMutableList())
    var shopName by mutableStateOf("")
    var category by mutableStateOf("")

    var barcodeData by mutableStateOf("")


    var isError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var isLoading by mutableStateOf(true)

    fun addStandardCard(token: String,shopId: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = cardRepository.addStandardCard(token,shopId,barcodeData)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    myCards = (myCards + response.body()!!) as MutableList<CardResponse>
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun addUnknownCard(token: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = cardRepository.addUnknownCard(token,barcodeData,shopName,category)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    myCards = (myCards + response.body()!!) as MutableList<CardResponse>
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun fetchAllCards(token: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = cardRepository.getAllCards(token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    myCards = (response.body() as MutableList<CardResponse>?)!!
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun deleteCard(token: String, id: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = cardRepository.deleteCard(token, id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    myCards.forEach {
                        if (it.id == id) {
                            myCards.remove(it)
                        }
                    }
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun onBarcodeDataChange(data: String) {
        barcodeData = data
    }

    fun onShopNameChange(name: String) {
        shopName = name
    }

    fun onCategoryChange(category: String) {
        this.category = category
    }

    fun onError(message: String) {
        isError = true
        errorMessage = message
        isLoading = false
    }
}