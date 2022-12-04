package com.example.quickwallet.network.model

import com.example.quickwallet.domain.model.PhoneNumber
import com.example.quickwallet.domain.utils.DomainMapper

class PhoneNumberMapper: DomainMapper<PhoneNumberDto, PhoneNumber> {
    override fun mapFromDomainModel(domainModel: PhoneNumber): PhoneNumberDto = PhoneNumberDto(phone = domainModel.phone)
}