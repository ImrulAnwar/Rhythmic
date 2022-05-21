package com.example.rhythmic.di

import com.example.rhythmic.domain.util.UIFunctions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
        @Singleton
        @Provides
        fun provideUIFunctions() = UIFunctions()
}