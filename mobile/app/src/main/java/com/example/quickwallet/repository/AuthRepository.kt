package com.example.quickwallet.repository

import androidx.lifecycle.LiveData
import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber

interface AuthRepository {
    suspend fun phoneAuth(authData: AuthData): LiveData<String>
    suspend fun phoneAuthRequest(phone: PhoneNumber): String?
}