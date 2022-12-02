package com.example.quickwallet.network.auth.impl

import android.util.Log
import com.example.quickwallet.network.ServiceBuilder
import com.example.quickwallet.network.api.AuthApi
import com.example.quickwallet.network.auth.AuthService
import com.example.quickwallet.network.model.AuthDataDto
import com.example.quickwallet.network.model.PhoneNumberDto
import com.example.quickwallet.network.response.AuthPhoneResponse
import com.example.quickwallet.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthServiceImpl: AuthService {
    private val retrofit: AuthApi = ServiceBuilder.buildService(AuthApi::class.java)
    override suspend fun phoneAuthRequest(phoneNumber: PhoneNumberDto): String? {
        var description: String? = null
        retrofit.phoneAuthRequest(phoneNumber).enqueue(
            object : Callback<String?> {
                override fun onFailure(call: Call<String?>, t: Throwable) {
                    description = null
                    Log.d(Constants.authLogTag, "phone request error")
                }

                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    description = response.body()
                    Log.d(Constants.authLogTag, "successfully send phone")
                }
            })
        return description
    }
    override suspend fun phoneAuth(authData: AuthDataDto): String?  {
        var token: String? = null
        retrofit.phoneAuth(authData).enqueue(
            object : Callback<AuthPhoneResponse> {
                override fun onFailure(call: Call<AuthPhoneResponse>, t: Throwable) {
                    token = null
                    (if (token != null) token else "cannot get token")?.let {
                        Log.d(
                            Constants.authLogTag,
                            it
                        )
                    }
                }
                override fun onResponse(call: Call<AuthPhoneResponse>, response: Response<AuthPhoneResponse>) {
                    token = response.body()?.token
                    token?.let { Log.d(Constants.authLogTag, it) }
                }
            })
        return token
    }
}