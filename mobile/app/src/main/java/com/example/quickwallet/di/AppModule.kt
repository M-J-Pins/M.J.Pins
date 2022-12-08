package com.example.quickwallet.di

import android.content.Context
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.storage.UserPersistentData
import com.example.quickwallet.storage.UserPersistentDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideUserPersistentData(app: BaseApplication): UserPersistentData = UserPersistentDataProvider(app)
}