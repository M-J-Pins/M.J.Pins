package com.example.quickwallet.di

import com.example.quickwallet.network.api.CardApi
import com.example.quickwallet.network.api.ShopsApi
import com.example.quickwallet.network.model.AuthDataMapper
import com.example.quickwallet.network.model.PhoneNumberMapper
import com.example.quickwallet.network.services.auth.AuthService
import com.example.quickwallet.repository.AuthRepository
import com.example.quickwallet.repository.CardRepository
import com.example.quickwallet.repository.ShopsRepository
import com.example.quickwallet.repository.impl.AuthRepositoryImpl
import com.example.quickwallet.repository.impl.CardRepositoryImpl
import com.example.quickwallet.repository.impl.ShopsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(
        authService: AuthService,
        phoneAuthMapper: AuthDataMapper,
        phoneMapper: PhoneNumberMapper
    ): AuthRepository = AuthRepositoryImpl(
        authService = authService,
        phoneAuthMapper = phoneAuthMapper,
        phoneMapper = phoneMapper
    )

    @Singleton
    @Provides
    fun provideCardRepository(
        cardApi: CardApi
    ): CardRepository = CardRepositoryImpl(cardApi)

    @Singleton
    @Provides
    fun provideShopsRepository(shopsApi: ShopsApi): ShopsRepository =
        ShopsRepositoryImpl(shopsApi)
}