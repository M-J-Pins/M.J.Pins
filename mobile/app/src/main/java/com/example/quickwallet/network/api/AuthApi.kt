package com.example.quickwallet.network.api


import com.example.quickwallet.network.model.AuthDataDto
import com.example.quickwallet.network.model.PhoneNumberDto
import com.example.quickwallet.network.response.AuthPhoneResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/auth_request")
    fun phoneAuthRequest(
        @Body phone: PhoneNumberDto
    ) : Call<String?>
    @POST("auth/auth")
    fun phoneAuth(
        @Body authData: AuthDataDto
    ) : Call<AuthPhoneResponse>
}