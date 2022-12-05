package com.example.quickwallet.domain.utils

interface DomainMapper<T,DomainModel> {
    fun mapFromDomainModel(domainModel: DomainModel): T
}