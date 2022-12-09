package com.example.quickwallet.network.api

import com.example.quickwallet.network.response.ShopCategoriesResponse
import com.example.quickwallet.network.response.ShopResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ShopsApi {

    @GET("shops/categories")
    suspend fun getShopCategories(): Response<ShopCategoriesResponse>

    @GET("shops")
    suspend fun getShops(): Response<List<ShopResponse>>

    @Multipart
    @POST("shops/similarity/most/v1")
    suspend fun getMostSimilarities(@Query("token")token: String, @Part file:MultipartBody.Part): Response<ShopResponse>
}