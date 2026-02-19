package com.margaritaolivera.almacenropa.features.inventory.di

import com.margaritaolivera.almacenropa.core.di.AppContainer
import com.margaritaolivera.almacenropa.features.inventory.data.repositories.InventoryRepositoryImpl
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.*
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.FormViewModelFactory
import com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels.InventoryViewModelFactory

class InventoryModule(private val appContainer: AppContainer) {

    private val repository by lazy {
        InventoryRepositoryImpl(appContainer)
    }

    fun provideInventoryViewModelFactory(): InventoryViewModelFactory {
        return InventoryViewModelFactory(
            GetPrendasUseCase(repository),
            DeletePrendaUseCase(repository),
            UpdateStockUseCase(repository)
        )
    }

    fun provideFormViewModelFactory(): FormViewModelFactory {
        return FormViewModelFactory(
            CreatePrendaUseCase(repository),
            GetPrendaByIdUseCase(repository),
            UpdatePrendaUseCase(repository)
        )
    }
}