package com.example.quickwallet.repository.impl

import com.example.quickwallet.network.api.ShopsApi
import com.example.quickwallet.network.response.ShopCategoriesResponse
import com.example.quickwallet.network.response.ShopResponse
import com.example.quickwallet.repository.ShopsRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ShopsRepositoryImpl(
    private val shopsApi: ShopsApi
):ShopsRepository {
    override suspend fun getShopCategories(): Response<ShopCategoriesResponse> = shopsApi.getShopCategories()
    override suspend fun getMostSimilarity(
        token: String,
        file: MultipartBody.Part
    ): Response<ShopResponse> = shopsApi.getMostSimilarities(token,file)
    override suspend fun getShops(): Response<List<ShopResponse>> = shopsApi.getShops()

}