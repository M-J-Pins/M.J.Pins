package com.example.quickwallet.network.services.auth

import com.example.quickwallet.network.model.AuthDataDto
import com.example.quickwallet.network.model.PhoneNumberDto


interface AuthService {
    suspend fun phoneAuthRequest(phoneNumber: PhoneNumberDto): String?
    suspend fun phoneAuth(authData: AuthDataDto): String?
}