package com.example.quickwallet.repository

import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber

interface AuthRepository {
    suspend fun phoneAuth(authData: AuthData): String?
    suspend fun phoneAuthRequest(phone: PhoneNumber): String?
}