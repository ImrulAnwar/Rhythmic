package com.example.rhythmic.di

import com.example.rhythmic.domain.repo.Repository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AdapterEntryPoint {
        var repository: Repository
}