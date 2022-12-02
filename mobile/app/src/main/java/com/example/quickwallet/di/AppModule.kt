package com.example.quickwallet.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.quickwallet.presentation.BaseApplication
import com.example.quickwallet.utils.Constants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
    @Named("token")
    fun provideAccessToken(@ApplicationContext app: Context): String? =
        app.getSharedPreferences(Constants.sharedPreferencesStorageName, MODE_PRIVATE)
            ?.getString(Constants.sharedPreferencesTokenName, null)
}