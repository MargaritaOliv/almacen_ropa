package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class UpdateStockUseCase @Inject constructor(private val repository: InventoryRepository) {
    suspend operator fun invoke(id: Int, cantidad: Int): Result<Prenda> = repository.updateStock(id, cantidad)
}