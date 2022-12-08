package com.example.quickwallet.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.domain.model.Shop
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.ShopsRepository
import com.example.quickwallet.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopsViewModel
@Inject constructor(
    private val shopsRepository: ShopsRepository,
    private val app: BaseApplication
) : ViewModel() {

    val shopsCategories: MutableState<MutableList<String>> = mutableStateOf(mutableListOf())
    val mostSimilarShop: MutableState<Shop?> = mutableStateOf(null)

    fun sendMostSimilarity(token: String, data:ByteArray){
        viewModelScope.launch(Dispatchers.IO){
            shopsRepository.getMostSimilarity(token,data)?.let {
                mostSimilarShop.value = it
            }
        }
    }

    fun fetchShopsCategories(){
        viewModelScope.launch(Dispatchers.IO){
            Log.d(Constants.shopsViewModelLogTag, ::fetchShopsCategories.name)
            shopsRepository.getShops()?.let {
                shopsCategories.value = it.toMutableList()
            }
        }
    }

}