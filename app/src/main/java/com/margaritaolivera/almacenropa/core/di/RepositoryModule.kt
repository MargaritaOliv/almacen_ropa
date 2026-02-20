package com.margaritaolivera.almacenropa.core.di

import com.margaritaolivera.almacenropa.features.auth.data.repositories.AuthRepositoryImpl
import com.margaritaolivera.almacenropa.features.auth.domain.repositories.AuthRepository
import com.margaritaolivera.almacenropa.features.inventory.data.repositories.InventoryRepositoryImpl
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        inventoryRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository
}