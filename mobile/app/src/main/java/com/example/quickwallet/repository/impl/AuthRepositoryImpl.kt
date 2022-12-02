package com.example.quickwallet.repository.impl


import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.model.PhoneNumber
import com.example.quickwallet.domain.utils.DomainMapper
import com.example.quickwallet.network.auth.AuthService
import com.example.quickwallet.network.model.AuthDataDto
import com.example.quickwallet.network.model.PhoneNumberDto
import com.example.quickwallet.repository.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val phoneAuthMapper: DomainMapper<AuthDataDto,AuthData>,
    private val phoneMapper: DomainMapper<PhoneNumberDto, PhoneNumber>
): AuthRepository {
    override suspend fun phoneAuth(authData: AuthData): String? = authService.phoneAuth(phoneAuthMapper.mapFromDomainModel(authData))
    override suspend fun phoneAuthRequest(phone: PhoneNumber): String? = authService.phoneAuthRequest(phoneMapper.mapFromDomainModel(phone))
}