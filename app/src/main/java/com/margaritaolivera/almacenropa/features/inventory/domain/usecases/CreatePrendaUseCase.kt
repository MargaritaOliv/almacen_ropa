package com.margaritaolivera.almacenropa.features.inventory.domain.usecases

import com.margaritaolivera.almacenropa.features.inventory.domain.entities.Prenda
import com.margaritaolivera.almacenropa.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class CreatePrendaUseCase @Inject constructor(private val repository: InventoryRepository) {
    suspend operator fun invoke(prenda: Prenda): Result<Prenda> {
        return repository.createPrenda(prenda)
    }
}