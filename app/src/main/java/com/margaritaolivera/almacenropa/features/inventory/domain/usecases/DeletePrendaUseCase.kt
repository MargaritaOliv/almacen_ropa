package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository

class DeletePrendaUseCase(private val repository: InventoryRepository) {
    suspend operator fun invoke(id: Int): Result<Boolean> = repository.deletePrenda(id)
}