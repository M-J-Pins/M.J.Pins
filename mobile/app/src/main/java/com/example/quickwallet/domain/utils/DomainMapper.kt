package com.example.quickwallet.domain.utils

interface DomainMapper<T,DomainModel> {
    fun mapToDomainModel(model: T): DomainModel
    fun mapFromDomainModel(domainModel: DomainModel): T
}