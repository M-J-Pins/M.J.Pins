package com.example.quickwallet.network.model

import com.example.quickwallet.domain.model.AuthData
import com.example.quickwallet.domain.utils.DomainMapper

class AuthDataMapper: DomainMapper<AuthDataDto, AuthData> {
    override fun mapToDomainModel(model: AuthDataDto): AuthData = AuthData(phoneNumber = model.phone, code = model.code)
    override fun mapFromDomainModel(domainModel: AuthData): AuthDataDto = AuthDataDto(phone = domainModel.phoneNumber, code = domainModel.code)
}