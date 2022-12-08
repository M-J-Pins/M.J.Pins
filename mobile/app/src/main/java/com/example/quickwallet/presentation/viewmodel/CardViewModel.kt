package com.example.quickwallet.presentation.viewmodel

import android.location.LocationManager
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CardViewModel
@Inject constructor(
    private val cardRepository: CardRepository,
    private val app: BaseApplication
) : ViewModel() {

    val quickCards: MutableState<MutableList<Card>> = mutableStateOf(mutableListOf())
    val myCards: MutableState<List<Card>> = mutableStateOf(listOf())
    val searchText = mutableStateOf("")

    fun onSearchTextChanged(text: String){
        searchText.value = text
    }

    fun fetchAllCards(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.getAllCards(token)?.let {
                myCards.value = it.toMutableList()
            }
        }
    }

    fun getQuickCards(token: String) {
        viewModelScope.launch(Dispatchers.IO){
            val locationServices = LocationServices.getFusedLocationProviderClient(app.applicationContext)
            val location = locationServices.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY,null)
            location.addOnSuccessListener {
                it?.let {
                    async{cardRepository.getQuickCards(token,it.latitude, it.latitude)?.let {
                        quickCards.value = it
                    }}
                }
            }
        }
    }

}