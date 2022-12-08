package com.example.quickwallet.network.services.auth.impl

import android.util.Log
import com.example.quickwallet.network.api.AuthApi
import com.example.quickwallet.network.model.AuthDataDto
import com.example.quickwallet.network.model.PhoneNumberDto
import com.example.quickwallet.network.services.auth.AuthService
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.storage.UserPersistentData
import com.example.quickwallet.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthServiceImpl constructor(
    private val retrofit: AuthApi,
    private val app: BaseApplication,
    private val userPersistentData: UserPersistentData
) : AuthService {

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

    override suspend fun phoneAuth(authData: AuthDataDto): String? {
        return retrofit.phoneAuth(authData).execute().body()?.token
    }
}