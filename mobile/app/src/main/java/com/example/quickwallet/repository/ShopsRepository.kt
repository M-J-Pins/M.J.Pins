package com.example.quickwallet.repository

import com.example.quickwallet.network.response.ShopCategoriesResponse
import com.example.quickwallet.network.response.ShopResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface ShopsRepository {
    suspend fun getShopCategories(): Response<ShopCategoriesResponse>
    suspend fun getMostSimilarity(token: String, file:MultipartBody.Part): Response<ShopResponse>
    suspend fun getShops(): Response<List<ShopResponse>>

}