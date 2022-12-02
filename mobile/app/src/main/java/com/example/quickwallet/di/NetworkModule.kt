package com.example.quickwallet.di

import com.example.quickwallet.network.ServiceBuilder
import com.example.quickwallet.network.api.AuthApi
import com.example.quickwallet.network.auth.AuthService
import com.example.quickwallet.network.auth.impl.AuthServiceImpl
import com.example.quickwallet.network.model.AuthDataMapper
import com.example.quickwallet.network.model.PhoneNumberMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideAuthDataMapper() : AuthDataMapper = AuthDataMapper()

    @Singleton
    @Provides
    fun providePhoneNumberMapper() : PhoneNumberMapper = PhoneNumberMapper()

    @Singleton
    @Provides
    fun provideAuthApi(): AuthApi = ServiceBuilder.buildService(AuthApi::class.java)
}