package com.margaritaolivera.almacenropa.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.DeletePrendaUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.GetPrendasUseCase
import com.margaritaolivera.almacenropa.features.inventory.domain.usecases.UpdateStockUseCase

class InventoryViewModelFactory(
    private val getPrendasUseCase: GetPrendasUseCase,
    private val deletePrendaUseCase: DeletePrendaUseCase,
    private val updateStockUseCase: UpdateStockUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(
                getPrendasUseCase,
                deletePrendaUseCase,
                updateStockUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}