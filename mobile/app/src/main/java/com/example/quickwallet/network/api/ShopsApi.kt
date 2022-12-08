package com.example.quickwallet.network.api

import com.example.quickwallet.network.response.ShopCategoriesResponse
import com.example.quickwallet.network.response.ShopResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ShopsApi {
    @GET("shops/categories")
    fun getShopCategories(): Call<ShopCategoriesResponse>


    @Multipart
    @POST("shops/similarity/most/v1")
    fun getMostSimilarities(@Query("token")token: String, @Part photo: MultipartBody.Part): Call<ShopResponse>

}