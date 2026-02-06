package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

class UpdateStockUseCase(private val repository: InventoryRepository) {
    // cantidad puede ser positivo (sumar) o negativo (restar)
    suspend operator fun invoke(id: Int, cantidad: Int): Result<Prenda> = repository.updateStock(id, cantidad)
}