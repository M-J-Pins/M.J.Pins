package com.example.quickwallet.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickwallet.domain.model.Shop
import com.example.quickwallet.network.response.ShopResponse
import com.example.quickwallet.network.response.toShop
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.repository.ShopsRepository
import com.example.quickwallet.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel
@Inject constructor(
    private val shopsRepository: ShopsRepository,
    private val app: BaseApplication
) : ViewModel() {

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    var isError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var isLoading by mutableStateOf(true)

    private val _shopCategories = mutableStateOf(emptyList<String>().toMutableList())
    val shopCategories: State<List<String>> = _shopCategories

    private val _shopCategoriesSelectedItem = mutableStateOf("")
    val shopCategoriesSelectedItem: State<String> = _shopCategoriesSelectedItem


    private val _shops = mutableStateOf(emptyList<ShopResponse>())
    val shops: State<List<ShopResponse>> = _shops

    var mostSimilar by mutableStateOf(ShopResponse("", "", "", "", "", ""))
    var filepath by mutableStateOf("")



    fun fetchShopCategories() {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = shopsRepository.getShopCategories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _shopCategories.value = response.body()!!.categories.toMutableList()
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun fetchAllShops() {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = shopsRepository.getShops()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _shops.value = response.body()!!.toMutableList()
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun getMostSimilarities(token: String, file: File) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val body = MultipartBody.Part.createFormData(
                "profile_picture",
                file.name,
                file.asRequestBody()
            )
            val response = shopsRepository.getMostSimilarity(token, body)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    mostSimilar = response.body()!!
                    isLoading = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun getPhotoFilepath(file: File) {
        filepath = file.absolutePath
    }

    fun onShopCategoryUpdate(data: String) {
        _shopCategoriesSelectedItem.value = data
    }

    fun onError(message: String) {
        isError = true
        errorMessage = message
        isLoading = false
    }


}