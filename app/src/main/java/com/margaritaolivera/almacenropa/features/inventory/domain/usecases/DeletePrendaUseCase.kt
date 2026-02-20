package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class DeletePrendaUseCase @Inject constructor(private val repository: InventoryRepository) {
    suspend operator fun invoke(id: Int): Result<Boolean> = repository.deletePrenda(id)
}