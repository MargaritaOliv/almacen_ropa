package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda

class GetPrendasUseCase(private val repository: InventoryRepository) {
    suspend operator fun invoke(): Result<List<Prenda>> = repository.getPrendas()
}