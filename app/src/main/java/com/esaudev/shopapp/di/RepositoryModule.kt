package com.esaudev.shopapp.di

import com.esaudev.shopapp.data.repository.DefaultAuthRepository
import com.esaudev.shopapp.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        productRepository: DefaultAuthRepository
    ): AuthRepository
}